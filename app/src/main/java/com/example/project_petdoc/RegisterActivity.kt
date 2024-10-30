package com.example.animalhospital

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project_petdoc.databinding.PetsSumBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: PetsSumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the binding and set the content view
        binding = PetsSumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 등록 버튼 클릭 시 데이터 전송
        binding.btnSign.setOnClickListener {
            val intent = Intent().apply {
                putExtra("category", binding.editCategory.text.toString())
                putExtra("name", binding.editName.text.toString())
                putExtra("gender", binding.editS.text.toString())
                putExtra("age", binding.editAge.text.toString().toInt()) // String을 Int로 변환
                putExtra("hospital", binding.editHos.text.toString())
            }
            setResult(RESULT_OK, intent)
            finish() // RegisterActivity 종료
        }
    }
}
