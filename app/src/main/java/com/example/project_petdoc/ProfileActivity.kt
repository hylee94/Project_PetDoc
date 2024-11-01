package com.example.project_petdoc

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.project_petdoc.client.MemberClient
import com.example.project_petdoc.databinding.DialogProfileBinding
import com.example.project_petdoc.pets.PetsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private lateinit var shared : SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        shared = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)

      //  val backButton = findViewById<ImageView>(R.id.backButton)
        val editPersonalInfoButton = findViewById<Button>(R.id.editPersonalInfoButton)
        val managePetButton = findViewById<Button>(R.id.managePetButton)
        val deleteAccountButton = findViewById<TextView>(R.id.deleteAccountButton)
        val btnProfileBack = findViewById<ImageView>(R.id.btnProfileBack)


        editPersonalInfoButton.setOnClickListener {
            showEditPersonalInfoDialog()
        }

        managePetButton.setOnClickListener {
            val intent = Intent(this, PetsActivity::class.java)
            startActivity(intent)
        }

        deleteAccountButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        btnProfileBack.setOnClickListener {
            finish()
        }
    }

    private fun showEditPersonalInfoDialog() {
        // 다이얼로그를 위한 레이아웃 인플레이트
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_profile, null)
        val binding = DialogProfileBinding.bind(dialogView)

        // AlertDialog 생성
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
        val alertDialog = dialogBuilder.create()

        // "수정" 버튼 클릭 리스너
        binding.saveButton.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()

            // 이메일과 비밀번호 처리 로직 추가
            if (email.isNotBlank() && password.isNotBlank()) {
                Toast.makeText(this, "이메일: $email\n비밀번호: $password", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "모든 필드를 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
        }

        // "취소" 버튼 클릭 리스너
        binding.cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
    private fun showDeleteConfirmationDialog(){
        val userId = shared.getString("userId", null) //사용자 ID 가져오기

        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("회원 탈퇴")
            .setMessage("정말로 탈퇴하시겠습니까?")
            .setPositiveButton("확인"){_, _ ->
                if (userId != null){
                    MemberClient.retrofit.delete(userId).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@ProfileActivity, "회원탈퇴 성공", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@ProfileActivity, "회원탈퇴 실패: ${response.message()}" , Toast.LENGTH_SHORT).show()
                            }
                        }



                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(this@ProfileActivity, "오류 발생: ${t.message}", Toast.LENGTH_SHORT).show()
                        }

                    })
                }else {
                    Toast.makeText(this@ProfileActivity, "사용자 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }

            }
            .setNegativeButton("취소", null)

        dialogBuilder
            .create()
            .show()
    }
}