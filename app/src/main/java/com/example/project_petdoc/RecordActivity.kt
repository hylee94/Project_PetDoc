package com.example.project_petdoc

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.project_petdoc.client.RecordClient
import com.example.project_petdoc.databinding.ActivityRecordBinding
import com.example.project_petdoc.dataclass.Member
import com.example.project_petdoc.dataclass.Pet
import com.example.project_petdoc.dataclass.Record
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecordBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var petId: Int? = null // petId 변수 추가

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent에서 petId 값을 받아옴
        petId = intent.getIntExtra("petId", -1) // 기본값으로 -1 사용

        Log.d("petId ", petId.toString())

        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)

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
                else -> saveRecord()
            }
        }
    }

    // Record 객체를 생성하고 서버에 저장하는 함수
    private fun saveRecord() {
        // SharedPreferences에서 사용자 정보를 가져와 Member 객체 생성
        val userId = sharedPreferences.getString("userId", null) ?: "기본값"
        val email = sharedPreferences.getString("userEmail", null) ?: "기본값"
        val password = sharedPreferences.getString("userPassword", null) ?: "기본값"

        // Member 객체 생성
        val member = Member(userId, email, password)

        // Pet 객체 생성 시 member를 전달
        val pet = Pet(
            petid = petId ?: 0,  // petId가 null일 경우 기본값 0으로 대체
            memberid = member,
            type = "type",
            name = "name",
            gender = "gender",
            age = 1,
            hospital = "hospital"
        )
        Log.d("pet ", pet.petid.toString())

        // Record 객체 생성
        val record = Record(
            no = 0,
            petid = pet,
            date = binding.edtDate.text.toString(),
            disease = binding.edtDisease.text.toString(),
            doctor_op = binding.edtOpinion.text.toString(),
            medicine = binding.edtPrescription.text.toString(),
            fee = binding.edtFee.text.toString(),
            hospital = binding.edtHospital.text.toString(),
            memo = binding.edtMemo.text.toString()
        )

        // RecordClient를 통해 데이터베이스에 저장
        RecordClient.retrofit.saveRecord(record).enqueue(object : Callback<Record> {
            override fun onResponse(call: Call<Record>, response: Response<Record>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RecordActivity, "저장 성공!", Toast.LENGTH_SHORT).show()

                    // MedicalActivity에 전달할 데이터 (리사이클러뷰에 표시할 데이터)
                    val resultIntent = Intent(this@RecordActivity, MedicalActivity::class.java).apply {
                        putExtra("date", binding.edtDate.text.toString())
                        putExtra("disease", binding.edtDisease.text.toString())
                        putExtra("opinion", binding.edtOpinion.text.toString())
                    }
                    startActivity(resultIntent) // MedicalActivity로 전환
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

    // 유효성 검사 실패 시 경고창 표시 함수
    private fun showAlert(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}