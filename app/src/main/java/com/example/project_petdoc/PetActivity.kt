package com.example.animalhospital

import Pet
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_petdoc.databinding.PetsMainBinding


class PetActivity : AppCompatActivity() {
    val binding by lazy { PetsMainBinding.inflate(layoutInflater) }
    val petList = ArrayList<Pet>()
    val petAdapter = PetAdapter(petList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Edge-to-Edge 패딩 설정
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // RecyclerView 초기화
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = petAdapter

        // "추가하기" 버튼 클릭 시 RegisterActivity로 이동
        binding.btnSign.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            registerActivityResultLauncher.launch(intent)
        }

        // 메인 화면으로 돌아가는 ImageView 클릭 시 액션
        binding.imageView2.setOnClickListener {
            // 메인 화면 기능 추가 (필요한 경우)
        }
    }

    // 등록된 데이터를 받아서 RecyclerView에 추가하는 launcher 설정
    private val registerActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                result.data?.let { data ->
                    val category = data.getStringExtra("category")
                    val name = data.getStringExtra("name")
                    val gender = data.getStringExtra("gender")
                    val ageString = data.getStringExtra("age") // String으로 가져옴
                    val hospital = data.getStringExtra("hospital")

                    val age = ageString?.toIntOrNull() // 변환 실패 시 null이 될 수 있음

                    petList.add(Pet(category, name, gender, age, hospital))
                    petAdapter.notifyDataSetChanged()
                }
            }
        }
}