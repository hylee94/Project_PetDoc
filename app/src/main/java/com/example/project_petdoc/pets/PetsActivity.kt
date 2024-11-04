package com.example.project_petdoc.pets

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_petdoc.Member.MainActivity
import com.example.project_petdoc.ProfileActivity
import com.example.project_petdoc.R
import com.example.project_petdoc.databinding.PetsListBinding
import com.example.project_petdoc.dataclass.Member
import com.example.project_petdoc.dataclass.Pet
import com.example.project_petdoc.MedicalActivity
import retrofit2.Call
import retrofit2.Response

class PetsActivity : AppCompatActivity() {
    private val binding by lazy { PetsListBinding.inflate(layoutInflater) }
    private val petList = ArrayList<Pet>()
    private val petAdapter = PetAdapter(petList) { selectedPet ->
        // 클릭 시 MedicalActivity로 이동
        val intent = Intent(this, MedicalActivity::class.java).apply {
            putExtra("petId", selectedPet.petid)
            putExtra("petName", selectedPet.name)
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Edge-to-Edge 패딩 설정
        ViewCompat.setOnApplyWindowInsetsListener(binding.PetsList) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 프로필 버튼 클릭 리스너
        val btnProfile = findViewById<ImageView>(R.id.btnProfile)
        btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // 로그아웃 버튼 클릭 리스너
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // RecyclerView 초기화
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = petAdapter

        // 삭제 클릭 리스너 설정
        petAdapter.onDeleteClick = { position ->
            if (position in petList.indices) {
                showDeleteConfirmationDialog(position)
            }
        }

        // "추가하기" 버튼 클릭 시 RegisterActivity로 이동
        binding.btnSign.setOnClickListener {
            registerActivityResultLauncher.launch(Intent(this, RegisterActivity::class.java))
        }

        // 애완동물 목록 가져오기
        loadPets()
    }

    private fun loadPets() {
        PetClient.retrofit.findAll().enqueue(object : retrofit2.Callback<List<Pet>> {
            override fun onResponse(call: Call<List<Pet>>, response: Response<List<Pet>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        petList.clear() // 기존 리스트 클리어
                        petList.addAll(it) // 새 리스트 추가
                        petAdapter.notifyDataSetChanged() // 어댑터 갱신
                    }
                } else {
                    // 서버에서 에러 발생 시 처리
                    showErrorDialog("Failed to load pets.")
                }
            }

            override fun onFailure(call: Call<List<Pet>>, t: Throwable) {
                // 네트워크 실패 처리
                showErrorDialog("Network error: ${t.message}")
            }
        })
    }

    private val registerActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                result.data?.let { data ->
                    val type = data.getStringExtra("type") ?: ""
                    val name = data.getStringExtra("name") ?: ""
                    val gender = data.getStringExtra("gender") ?: ""
                    val age = data.getIntExtra("age", 0)
                    val hospital = data.getStringExtra("hospital") ?: ""

                    petList.add(Pet(0, Member("id", "email", "password"), type, name, gender, age, hospital))
                    petAdapter.notifyDataSetChanged()
                }
            }
        }

    private fun showDeleteConfirmationDialog(position: Int) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("정말 삭제하시겠습니까?")
            .setCancelable(false)
            .setPositiveButton("예") { _, _ ->
                val petId = petList[position].petid // 삭제할 애완동물 ID 가져오기
                deletePet(petId, position) // 삭제 메서드 호출
            }
            .setNegativeButton("아니요") { dialog, _ -> dialog.dismiss() }
        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun deletePet(petId: Int, position: Int) {
        PetClient.retrofit.deleteById(petId.toString()).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    petList.removeAt(position) // 어댑터에서 아이템 제거
                    petAdapter.notifyItemRemoved(position) // 어댑터 갱신
                } else {
                    // 삭제 실패 처리
                    showErrorDialog("Failed to delete pet.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // 네트워크 실패 처리
                showErrorDialog("Network error: ${t.message}")
            }
        })
    }

    private fun showErrorDialog(message: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
        val alert = dialogBuilder.create()
        alert.show()
    }
}
