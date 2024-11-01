package com.example.project_petdoc


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_petdoc.databinding.ActivityMedicalBinding
import com.example.project_petdoc.dataclass.Medical
import com.example.project_petdoc.dataclass.Record
import com.example.project_petdoc.client.RecordClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MedicalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicalBinding
    private lateinit var medicalList: MutableList<Record> // 리스트를 변경 가능한 MutableList로 선언
    private lateinit var medicalAdapter: MedicalAdapter

    // ActivityResultLauncher 등록
//    private val recordActivityLauncher: ActivityResultLauncher<Intent> =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK) {
//                val date = result.data?.getStringExtra("date")
//                val disease = result.data?.getStringExtra("disease")
//                val opinion = result.data?.getStringExtra("opinion")
//
//                if (date != null && disease != null && opinion != null) {
//                    val newRecord = Medical(disease, date, opinion)
//                    medicalList.add(newRecord)
//                    medicalAdapter.notifyItemInserted(medicalList.size - 1)
//                }
//            }
//        }
    private val recordActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                fetchMedicalRecords() // 새로운 데이터가 추가되었으므로 리스트를 다시 불러옴
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding 설정
        binding = ActivityMedicalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 설정
        medicalList = mutableListOf()
        medicalAdapter = MedicalAdapter(medicalList) { selectedRecord ->
            // 항목 클릭 시 Record2Activity로 이동
            val intent = Intent(this, RecordActivity2::class.java).apply {
                putExtra("date", selectedRecord.date)
                putExtra("disease", selectedRecord.disease)
                putExtra("opinion", selectedRecord.doctor_op)
                putExtra("prescription", selectedRecord.medicine)
                putExtra("fee", selectedRecord.fee)
                putExtra("hospital", selectedRecord.hospital)
                putExtra("memo", selectedRecord.memo)
            }
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = medicalAdapter

        // 추가하기 버튼을 클릭하면 RecordActivity로 이동
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            recordActivityLauncher.launch(intent)
        }

        // 서버에서 데이터 가져오기
        fetchMedicalRecords()
    }

    // 서버에서 데이터 가져와 리사이클러뷰 업데이트
    private fun fetchMedicalRecords() {
        RecordClient.retrofit.getRecords().enqueue(object : Callback<List<Record>> {
            override fun onResponse(call: Call<List<Record>>, response: Response<List<Record>>) {
                if (response.isSuccessful) {
                    response.body()?.let { records ->
                        medicalList.clear()
                        medicalList.addAll(records)
                        medicalAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<Record>>, t: Throwable) {
                // 서버 오류 처리
                // 예를 들어, Log 메시지 출력 또는 사용자에게 알림 표시
                Log.e("MedicalActivity", "서버 연결 실패: ${t.message}")
            }
        })
        // 추가하기 버튼을 클릭하면 RecordActivity로 이동
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            recordActivityLauncher.launch(intent) // ActivityResultLauncher로 RecordActivity 시작
        }
    }
}
