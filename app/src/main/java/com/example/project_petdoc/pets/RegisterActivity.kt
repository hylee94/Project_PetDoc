package com.example.project_petdoc.pets

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_petdoc.R
import com.example.project_petdoc.databinding.PetsSumBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: PetsSumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Inflate the binding and set the content view
        binding = PetsSumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.petback.setOnClickListener {
            finish()
        }

        // 등록 버튼 클릭 시 데이터 전송
        binding.btnSign.setOnClickListener {
            val type = binding.editType.text.toString().trim()
            val name = binding.editName.text.toString().trim()
            val gender = binding.editS.text.toString().trim()
            val age = binding.editAge.text.toString().trim()
            val hospital = binding.editHos.text.toString().trim()

            if (type.isEmpty()) {
                binding.editType.error = "종을 입력해주세요"
            } else if (name.isEmpty()) {
                binding.editName.error = "이름을 입력해주세요"
            } else if (gender.isEmpty()) {
                binding.editS.error = "성별을 입력해주세요"
            } else if (gender != "남" && gender != "여") {
                binding.editS.error = "성별은 '남' 또는 '여'만 입력 가능합니다"
            } else if (age.isEmpty()) {
                binding.editAge.error = "나이를 입력해주세요"
            } else if (!age.matches(Regex("^[0-9]+살$"))) {
                binding.editAge.error = "ex)3살 이렇게 입력해주세요"
            } else if (hospital.isEmpty()) {
                binding.editHos.error = "종 구분 입력해주세요"
            } else {
                val intent = Intent().apply {
                    putExtra("type", type)
                    putExtra("name", name)
                    putExtra("gender", gender)
                    putExtra("age", age)
                    putExtra("hospital", hospital)
                }
                AlertDialog.Builder(this)
                    .setTitle("등록 확인")
                    .setMessage("입력하신 내용을 등록하시겠습니까?")
                    .setPositiveButton("확인") { _, _ ->
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                    .setNegativeButton("취소", null)
                    .show()
            }

        }
    }
}
