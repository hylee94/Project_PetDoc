package com.example.project_petdoc

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.project_petdoc.databinding.DialogProfileBinding
import com.example.project_petdoc.pets.PetsActivity

class ProfileActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

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
            Toast.makeText(this, "Delete Account", Toast.LENGTH_SHORT).show()
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
}