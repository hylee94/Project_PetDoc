package com.example.project_petdoc.pets

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_petdoc.Member.MainActivity
import com.example.project_petdoc.ProfileActivity
import com.example.project_petdoc.R
import com.example.project_petdoc.databinding.PetsListBinding
import com.example.project_petdoc.dataclass.Pet


class PetsActivity : AppCompatActivity() {
    val binding by lazy { PetsListBinding.inflate(layoutInflater) }
    val petList = ArrayList<Pet>()
    val petAdapter = PetAdapter(petList)

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

        val btnProfile = findViewById<ImageView>(R.id.btnProfile)
        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

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

        // "추가하기" 버튼 클릭 시 RegisterActivity로 이동
        binding.btnSign.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            registerActivityResultLauncher.launch(intent)
        }
    }

    // 등록된 데이터를 받아서 RecyclerView에 추가하는 launcher 설정
    private val registerActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                result.data!!.let { data ->
                    val type = data.getStringExtra("type")
                    val name = data.getStringExtra("name")
                    val gender = data.getStringExtra("gender")
                    val age = data.getStringExtra("age")
                    val hospital = data.getStringExtra("hospital")

                    petList.add(Pet(type, name, gender, age, hospital))
                    petAdapter.notifyDataSetChanged()
                }
            }
        }
}