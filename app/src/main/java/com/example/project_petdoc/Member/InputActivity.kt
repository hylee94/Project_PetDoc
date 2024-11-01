package com.example.project_petdoc.Member

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_petdoc.R
import com.example.project_petdoc.client.MemberClient
import com.example.project_petdoc.databinding.ActivityInputBinding
import com.example.project_petdoc.dataclass.Member
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class InputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //(R.layout.activity_input)
        val binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 입력 데이터 전송
        binding.btnSignUp.setOnClickListener {
            val id = binding.editJoinId.text.toString()
            val password = binding.editJoinPw.text.toString()
            val email = binding.editJoinEmail.text.toString()
            if (id.isNotBlank() && password.isNotBlank() && email.isNotBlank()) {
                val member = Member(id, password, email)
                registerMember(member)
            } else {
                Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }


            binding.btnCancle.setOnClickListener {
            finish()
        }




    }

    // 회원가입 메서드
    private fun registerMember(member: Member) {
        val call = MemberClient.retrofit.insert(member)
        call.enqueue(object : Callback<Member>{
            override fun onResponse(call: Call<Member>, response: Response<Member>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@InputActivity, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
            }else {
                    Toast.makeText(this@InputActivity, "회원가입 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Member>, t: Throwable) {
                Toast.makeText(this@InputActivity, "오류 발생: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}