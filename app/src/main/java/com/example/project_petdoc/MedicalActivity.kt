package com.example.teamproject

import MedicalAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_petdoc.R
import com.example.project_petdoc.RecordActivity2
import com.example.project_petdoc.databinding.ActivityMedicalBinding
import com.example.project_petdoc.dataclass.Medical

class MedicalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicalBinding
    private lateinit var medicalList: MutableList<Medical> // 리스트를 변경 가능한 MutableList로 선언
    private lateinit var medicalAdapter: MedicalAdapter

    // ActivityResultLauncher 등록
    private val recordActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val date = result.data?.getStringExtra("date")
                val disease = result.data?.getStringExtra("disease")
                val opinion = result.data?.getStringExtra("opinion")

                if (date != null && disease != null && opinion != null) {
                    val newRecord = Medical(disease, date, opinion)
                    medicalList.add(newRecord)
                    medicalAdapter.notifyItemInserted(medicalList.size - 1)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // View Binding 설정
        binding = ActivityMedicalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Sample data 생성
        medicalList = mutableListOf(
            Medical("감기", "2024-10-29", "기침 및 목 아픔"),
            Medical("독감", "2024-11-05", "열과 두통 증상"),
            Medical("알레르기", "2024-11-10", "재채기 및 콧물")
        )

        // RecyclerView 설정
        medicalAdapter = MedicalAdapter(medicalList) { selectedMedical ->
            // 항목 클릭 시 상세보기 화면으로 이동
            val intent = Intent(this, RecordActivity2::class.java).apply {
                putExtra("date", selectedMedical.date)
                putExtra("disease", selectedMedical.disease)
                putExtra("opinion", selectedMedical.doctor_op)
                // 필요시 Record2Activity에 추가적인 정보 전달
            }
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = medicalAdapter

        // 추가하기 버튼을 클릭하면 RecordActivity로 이동
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            recordActivityLauncher.launch(intent) // ActivityResultLauncher로 RecordActivity 시작
        }
    }
}

