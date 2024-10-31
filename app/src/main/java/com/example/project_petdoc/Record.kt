package com.example.project_petdoc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Record : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        val edtDate = findViewById<EditText>(R.id.edtDate)
        val edtDisease = findViewById<EditText>(R.id.edtDisease)
        val edtOpinion = findViewById<EditText>(R.id.edtOpinion)
        val edtPrescription = findViewById<EditText>(R.id.edtPrescription)
        val edtFee = findViewById<EditText>(R.id.edtFee)
        val edtHospital = findViewById<EditText>(R.id.edtHospital)
        val edtMemo = findViewById<EditText>(R.id.edtMemo)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            when {
                edtDate.text.isBlank() -> showAlert("날짜를 입력해야 합니다")
                edtDisease.text.isBlank() -> showAlert("질환을 입력해야 합니다")
                edtOpinion.text.isBlank() -> showAlert("소견을 입력해야 합니다")
                edtPrescription.text.isBlank() -> showAlert("처방을 입력해야 합니다")
                edtFee.text.isBlank() -> showAlert("병원비를 입력해야 합니다")
                edtHospital.text.isBlank() -> showAlert("병원명을 입력해야 합니다")
                edtMemo.text.isBlank() -> showAlert("메모를 입력해야 합니다")
                else -> {
                    // 모든 필드가 입력된 경우에만 다음 화면으로 이동
                    val intent = Intent(this, Record2::class.java).apply {
                        putExtra("date", edtDate.text.toString())
                        putExtra("disease", edtDisease.text.toString())
                        putExtra("opinion", edtOpinion.text.toString())
                        putExtra("prescription", edtPrescription.text.toString())
                        putExtra("fee", edtFee.text.toString())
                        putExtra("hospital", edtHospital.text.toString())
                        putExtra("memo", edtMemo.text.toString())
                    }
                    startActivity(intent)

                    // 입력 필드 초기화
                    edtDate.text.clear()
                    edtDisease.text.clear()
                    edtOpinion.text.clear()
                    edtPrescription.text.clear()
                    edtFee.text.clear()
                    edtHospital.text.clear()
                    edtMemo.text.clear()
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


//테스트