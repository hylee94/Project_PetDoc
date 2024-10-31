package com.example.teamproject


import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_petdoc.R

class RecordActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_record2)
        //        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val dateText = findViewById<TextView>(R.id.dateText)
        val diseaseText = findViewById<TextView>(R.id.diseaseText)
        val opinionText = findViewById<TextView>(R.id.opinionText)
        val prescriptionText = findViewById<TextView>(R.id.prescriptionText)
        val feeText = findViewById<TextView>(R.id.feeText)
        val hospitalText = findViewById<TextView>(R.id.hospitalText)
        val memoText = findViewById<TextView>(R.id.memoText)

        val intent = intent
        dateText.text = intent.getStringExtra("date")
        diseaseText.text = intent.getStringExtra("disease")
        opinionText.text = intent.getStringExtra("opinion")
        prescriptionText.text = intent.getStringExtra("prescription")
        feeText.text = intent.getStringExtra("fee")
        hospitalText.text = intent.getStringExtra("hospital")
        memoText.text = intent.getStringExtra("memo")
    }
}