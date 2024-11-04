package com.example.project_petdoc.pets

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = PetsSumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null) ?: "기본값"
        val email = sharedPreferences.getString("email", null) ?: "기본값"
        val password = sharedPreferences.getString("password", null) ?: "기본값"

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
                Member(userId, email, password),
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

//                        response.body()?.let { petInfo ->
//                            saveUserCredentials(petInfo.petid, petInfo.memberid, petInfo.type,
//                                petInfo.name,petInfo.gender,petInfo.age,petInfo.hospital)
//                        }
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
        // Intent에서 기존 Pet 데이터를 받아와 초기화
        val petId = intent.getIntExtra("petId", -1)
        if (petId != -1) {
            binding.editType.setText(intent.getStringExtra("type"))
            binding.editName.setText(intent.getStringExtra("name"))
            binding.editS.setText(intent.getStringExtra("gender"))
            binding.editAge.setText(intent.getIntExtra("age", 0).toString())
            binding.editHos.setText(intent.getStringExtra("hospital"))
        }
        binding.btnSign.text = "수정" //수정 버튼 텍스트 설정
        binding.btnSign.setOnClickListener{
            val updatedPet = Pet(
                petId,
                // 현재 로그인된 멤버 정보를 사용하여 객체 생성
                Member(userId, email, password),
                binding.editType.text.toString(),
                binding.editName.text.toString(),
                binding.editS.text.toString(),
                binding.editAge.text.toString().toInt(),
                binding.editHos.text.toString()
            )
            // Retrofit을 통해 서버에 수정 요청
            PetClient.retrofit.update(petId.toString(), updatedPet).enqueue(object : Callback<Pet>{
                override fun onResponse(call: Call<Pet>, response: Response<Pet>) {
                    if (response.isSuccessful) {
                        // 수정이 성공적으로 완료되면 결과를 전달하고 액티비티 종료
                        val resultIntent = Intent().apply {
                            putExtra("petId", petId)
                            putExtra("type", updatedPet.type)
                            putExtra("name", updatedPet.name)
                            putExtra("gender", updatedPet.gender)
                            putExtra("age", updatedPet.age)
                            putExtra("hospital", updatedPet.hospital)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }else {
                        //오류 처리
                        Toast.makeText(this@RegisterActivity, "수정 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Pet>, t: Throwable) {
                    // 통신 실패 처리
                    Toast.makeText(this@RegisterActivity, "서버와의 연결 실패", Toast.LENGTH_SHORT).show()
                }

            })
            // 수정된 데이터를 Intent에 담아 전달
            val resultIntent = Intent().apply {
                putExtra("petId", petId)
                putExtra("type", binding.editType.text.toString())
                putExtra("name", binding.editName.text.toString())
                putExtra("gender", binding.editS.text.toString())
                putExtra("age", binding.editAge.text.toString().toInt())
                putExtra("hospital", binding.editHos.text.toString())
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

    }
//    private fun saveUserCredentials(petid: Int, memberid: Member, type: String, name:String,
//                                    gender:String, age:Int, hospital:String) {
//        val editor = sharedPreferences.edit()
//        editor.putInt("petid", petid)
//        editor.putString("memberid", memberid.toString())
//        editor.putString("type", type)
//        editor.putString("name", name)
//        editor.putString("gender", gender)
//        editor.putInt("age", age)
//        editor.putString("hospital", hospital)
//        editor.apply()
//    }
}
