package com.example.project_petdoc.Member

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_petdoc.R
import com.example.project_petdoc.client.MemberClient
import com.example.project_petdoc.databinding.ActivityMainBinding
import com.example.project_petdoc.dataclass.Member
import com.example.project_petdoc.pets.PetsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var shared : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //SharedPreferences 초기화
        shared = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)

        binding.btnJoin.setOnClickListener {
            val intent = Intent(this, InputActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val id = binding.editId.text.toString()
            val password = binding.editPw.text.toString()

            if (id.isNotBlank() && password.isNotBlank()) {
                val member = Member(id = id, email = "", password = password)

                MemberClient.retrofit.login(member).enqueue(object : Callback<Member> {
                    override fun onResponse(call: Call<Member>, response: Response<Member>) {
                        if (response.isSuccessful) {
                            // 로그인 성공 처리
                            Toast.makeText(this@MainActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                            // 로그인 상태 저장
                            saveLoginStatus(true)

                            response.body()?.let { memberInfo ->
                                saveUserCredentials(memberInfo.id, memberInfo.email, memberInfo.password )
                            }

                            // 다음 화면으로 이동하는 코드 추가
                            val intent = Intent(this@MainActivity, PetsActivity::class.java)
                            startActivity(intent)
                        } else {
                            // 로그인 실패 처리
                            Toast.makeText(this@MainActivity, "로그인 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Member>, t: Throwable) {
                        // 네트워크 오류 또는 서버 오류 처리
                        Toast.makeText(this@MainActivity, "오류 발생: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "ID와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }//onCreate

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val editor = shared.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    private fun saveUserCredentials(id: String, email: String, password: String) {
        val editor = shared.edit()
        editor.putString("userId", id)
        editor.putString("userEmail", email)
        editor.putString("userPassword", password)
        editor.apply()
    }

}