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
import com.example.project_petdoc.Member.MainActivity
import com.example.project_petdoc.client.MemberClient
import com.example.project_petdoc.databinding.DialogProfileBinding
import com.example.project_petdoc.dataclass.Member
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

        val userId = shared.getString("userId", null)

        val textViewUserid = findViewById<TextView>(R.id.textView4)
        textViewUserid.text = userId
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
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_profile, null)
        val binding = DialogProfileBinding.bind(dialogView)

        val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "")
        val userEmail = sharedPreferences.getString("userEmail", "")

        binding.editEmail.setText(userEmail)
        binding.editId.setText(userId)

        // AlertDialog 생성
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
        val alertDialog = dialogBuilder.create()

        // "수정" 버튼 클릭 리스너
        binding.saveButton.setOnClickListener {
            val id = binding.editId.text.toString()
            val email = binding.editEmail.text.toString()
            val newPassword = binding.editPassword.text.toString()

            if (email.isNotBlank() && newPassword.isNotBlank()) {
                val updatedMember = Member(id = id, email = email, password = newPassword)

                // 서버로 업데이트 요청
                MemberClient.retrofit.update(updatedMember, id).enqueue(object :
                    Callback<Member> {
                    override fun onResponse(call: Call<Member>, response: Response<Member>) {
                        if (response.isSuccessful) {
                            val editor = sharedPreferences.edit()
                            editor.putString("userId", id)
                            editor.putString("userEmail", email)
                            editor.putString("userPassword", newPassword)
                            editor.apply()

                            Toast.makeText(this@ProfileActivity, "개인 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                        } else {
                            Toast.makeText(this@ProfileActivity, "수정 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Member>, t: Throwable) {
                        Toast.makeText(this@ProfileActivity, "오류 발생: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "모든 필드를 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
        }

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
                                val intent = Intent(this@ProfileActivity, MainActivity::class.java)
                                startActivity(intent)
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