package com.example.project_petdoc.pets

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.project_petdoc.databinding.PetsSumBinding
import androidx.appcompat.app.AlertDialog
import com.example.project_petdoc.dataclass.Member
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
                binding.editType.error = "종을 입력해주세요"
            } else if (name.isEmpty()) {
                binding.editName.error = "이름을 입력해주세요"
            } else if (gender.isEmpty()) {
                binding.editS.error = "성별을 입력해주세요"
            } else if (gender != "남" && gender != "여") {
                binding.editS.error = "성별은 '남' 또는 '여'만 입력 가능합니다"
            } else if (age.isEmpty()) {
                binding.editAge.error = "나이를 입력해주세요"
//            } else if (!age.matches(Regex("^[0-9]+살$"))) {
//                binding.editAge.error = "ex)3살 이렇게 입력해주세요"
            } else if (hospital.isEmpty()) {
                binding.editHos.error = "병원을 입력해주세요"
            } else {
                // 나이 처리
                val a = age.replace("살", "").toIntOrNull()
                if (a == null) {
                    binding.editAge.error = "나이를 올바르게 입력해주세요"
                    return@setOnClickListener
                }

            }
            val numericAge = binding.editAge.text.toString().replace("살", "").toIntOrNull()
            if (numericAge == null) {
                binding.editAge.error = "나이를 올바르게 입력해주세요"
                return@setOnClickListener
            }
            val pet = Pet(
                0,
                Member("hy","hy","hy"),
                binding.editType.text.toString(),
                binding.editName.text.toString(),
                binding.editS.text.toString(),
                numericAge,
                binding.editHos.text.toString()
            )
            PetClient.retrofit.save(pet).enqueue(object : retrofit2.Callback<Pet>{
                override fun onResponse(call: Call<Pet>, response: Response<Pet>) {
                    if (response.isSuccessful) {
                        Log.d("retrofit insert : ", "${response.body()}")
                        intent.putExtra("type", response.body()!!.type)
                        intent.putExtra("name", response.body()!!.name)
                        intent.putExtra("gender", response.body()!!.gender)
                        intent.putExtra("age", response.body()!!.age)
                        intent.putExtra("hospital", response.body()!!.hospital)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                    else {
                        // 서버 응답 실패
                        Log.d("retrofit response code", "Code: ${response.code()}")
                        val errorBody = response.errorBody()?.string()
                        Log.d("retrofit error body", "Error: $errorBody")

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
