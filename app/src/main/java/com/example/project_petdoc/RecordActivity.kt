package com.example.teamproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_petdoc.R
import com.example.project_petdoc.databinding.ActivityRecordBinding

class RecordActivity : AppCompatActivity() {

    // ViewBinding 초기화
    private lateinit var binding: ActivityRecordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ViewBinding을 사용하여 레이아웃 설정
        binding = ActivityRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnRecordBack = findViewById<ImageView>(R.id.btnRecordBack)
        btnRecordBack.setOnClickListener {
            finish()
        }

        // 저장 버튼 클릭 리스너 설정
        binding.btnSave.setOnClickListener {
            when {
                binding.edtDate.text.isBlank() -> showAlert("날짜를 입력해야 합니다")
                binding.edtDisease.text.isBlank() -> showAlert("질환을 입력해야 합니다")
                binding.edtOpinion.text.isBlank() -> showAlert("소견을 입력해야 합니다")
                binding.edtPrescription.text.isBlank() -> showAlert("처방을 입력해야 합니다")
                binding.edtFee.text.isBlank() -> showAlert("병원비를 입력해야 합니다")
                binding.edtHospital.text.isBlank() -> showAlert("병원명을 입력해야 합니다")
                binding.edtMemo.text.isBlank() -> showAlert("메모를 입력해야 합니다")
                else -> {
                    // MedicalActivity에 전달할 기본 정보 (리사이클러뷰에 표시할 데이터)
                    val resultIntent = Intent().apply {
                        putExtra("date", binding.edtDate.text.toString())
                        putExtra("disease", binding.edtDisease.text.toString())
                        putExtra("opinion", binding.edtOpinion.text.toString())
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish() // MedicalActivity로 돌아가기

                    // 입력 필드 초기화
                    binding.edtDate.text.clear()
                    binding.edtDisease.text.clear()
                    binding.edtOpinion.text.clear()
                    binding.edtPrescription.text.clear()
                    binding.edtFee.text.clear()
                    binding.edtHospital.text.clear()
                    binding.edtMemo.text.clear()
                }
            }
        }
    }

    // 유효성 검사 실패 시 경고창 표시 함수
    private fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
