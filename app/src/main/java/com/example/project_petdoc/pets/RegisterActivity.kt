package com.example.project_petdoc.pets

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project_petdoc.databinding.PetsSumBinding
import androidx.appcompat.app.AlertDialog
import com.example.project_petdoc.dataclass.Pet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: PetsSumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PetsSumBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            // 유효성 검사
            if (type.isEmpty()) {
                binding.editType.error = "동물 종류를 입력해주세요"
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
                binding.editHos.error = "병원을 입력해주세요"
            } else {
                // 나이 처리
                val a = age.replace("살", "").toIntOrNull()
                if (a == null) {
                    binding.editAge.error = "나이를 올바르게 입력해주세요"
                    return@setOnClickListener
                }

                // Pet 객체 생성]
                val memberid = "user_unique_id"
                val pet = Pet(
                    petid = "",
                    memberid = memberid,
                    type = type,
                    name = name,
                    gender = gender,
                    age = a,
                    hospital = hospital
                )

                // 서버에 데이터 전송
                PetClient.retrofit.save(pet).enqueue(object : Callback<Pet> {
                    override fun onResponse(call: Call<Pet>, response: Response<Pet>) {
                        if (response.isSuccessful) {
                            AlertDialog.Builder(this@RegisterActivity)
                                .setTitle("등록 완료")
                                .setMessage("반려동물 정보가 성공적으로 등록되었습니다.")
                                .setPositiveButton("확인") { _, _ ->
                                    setResult(RESULT_OK)
                                    finish()
                                }
                                .show()
                        } else {
                            // 서버 응답 실패
                            AlertDialog.Builder(this@RegisterActivity)
                                .setTitle("등록 실패")
                                .setMessage("서버와의 통신에 실패했습니다. 다시 시도해주세요.")
                                .setPositiveButton("확인", null)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<Pet>, t: Throwable) {
                        // 네트워크 오류 또는 기타 이유로 통신 실패
                        AlertDialog.Builder(this@RegisterActivity)
                            .setTitle("등록 실패")
                            .setMessage("네트워크 오류가 발생했습니다: ${t.message}")
                            .setPositiveButton("확인", null)
                            .show()
                    }
                })
            }
        }
    }
}
