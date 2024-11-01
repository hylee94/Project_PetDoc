package com.example.project_petdoc

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.project_petdoc.client.RecordClient
import com.example.project_petdoc.databinding.ActivityRecordBinding
import com.example.project_petdoc.dataclass.Pet
import com.example.project_petdoc.dataclass.Record
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecordBinding

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    // 입력된 데이터를 기반으로 Record 객체 생성
                    val record = Record(
                        0,
                        Pet(1),
                        binding.edtDate.text.toString(),
                        binding.edtDisease.text.toString(),
                        binding.edtOpinion.text.toString(),
                        binding.edtPrescription.text.toString(),
                        binding.edtFee.text.toString(),
                        binding.edtHospital.text.toString(),
                        binding.edtMemo.text.toString()
                    )

                    // RecordClient를 통해 데이터베이스에 저장
                    RecordClient.retrofit.saveRecord(record).enqueue(object :Callback<Record> {
                        override fun onResponse(call: Call<Record>, response: Response<Record>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@RecordActivity, "저장 성공!", Toast.LENGTH_SHORT).show()

                                // MedicalActivity에 전달할 기본 정보 (리사이클러뷰에 표시할 데이터)
                                val resultIntent = Intent(this@RecordActivity, MedicalActivity::class.java).apply {
                                    putExtra("date", binding.edtDate.text.toString())
                                    putExtra("disease", binding.edtDisease.text.toString())
                                    putExtra("opinion", binding.edtOpinion.text.toString())
                                }
                                setResult(RESULT_OK, resultIntent) // MedicalActivity에 결과 전달
                                finish() // RecordActivity 종료 후 MedicalActivity로 돌아가기
                            } else {
                                Toast.makeText(this@RecordActivity, "저장 실패: 서버 오류", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Record>, t: Throwable) {
                            Toast.makeText(this@RecordActivity, "저장 실패: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
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
