package com.example.project_petdoc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_petdoc.databinding.ActivityMedicalBinding
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
    private val recordActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // 새로운 데이터가 추가되었으므로 리스트를 다시 불러오거나 Intent로 전달된 데이터를 추가
                val date = result.data?.getStringExtra("date")
                val disease = result.data?.getStringExtra("disease")
                val opinion = result.data?.getStringExtra("opinion")

                if (!date.isNullOrEmpty() && !disease.isNullOrEmpty() && !opinion.isNullOrEmpty()) {
                    // 새로운 Record 객체 생성 및 추가
                    val newRecord = Record(
                        no = 0,
                        pet = null,  // Pet 객체는 필요하지 않으므로 null 설정
                        date = date,
                        disease = disease,
                        doctor_op = opinion,
                        medicine = "",
                        fee = "",
                        hospital = "",
                        memo = ""
                    )
                    medicalList.add(newRecord)
                    medicalAdapter.notifyItemInserted(medicalList.size - 1)
                } else {
                    // 데이터가 유효하지 않을 경우 사용자에게 알림
                    showError("데이터가 유효하지 않습니다.")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding 설정
        binding = ActivityMedicalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 설정
        setupRecyclerView()

        // 추가하기 버튼 클릭 시 RecordActivity로 이동
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            recordActivityLauncher.launch(intent)
        }

        // 서버에서 데이터 가져오기
        fetchMedicalRecords()
    }

    // RecyclerView 설정 메소드
    private fun setupRecyclerView() {
        medicalList = mutableListOf()
        medicalAdapter = MedicalAdapter(medicalList) { selectedRecord ->
            // 항목 클릭 시 RecordActivity2로 이동
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
    }

    // 서버에서 데이터 가져와 RecyclerView 업데이트
    private fun fetchMedicalRecords() {
        RecordClient.retrofit.getRecords().enqueue(object : Callback<List<Record>> {
            override fun onResponse(call: Call<List<Record>>, response: Response<List<Record>>) {
                if (response.isSuccessful) {
                    response.body()?.let { records ->
                        medicalList.clear()
                        medicalList.addAll(records)
                        medicalAdapter.notifyDataSetChanged()
                    } ?: run {
                        showError("의료 기록을 불러오는 데 실패했습니다.")
                    }
                } else {
                    showError("서버 오류: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Record>>, t: Throwable) {
                // 서버 오류 처리
                Log.e("MedicalActivity", "서버 연결 실패: ${t.message}")
                showError("네트워크 오류: ${t.message}")
            }
        })
    }

    // 사용자에게 오류 메시지 표시
    private fun showError(message: String) {
        // 여기에서 Toast나 Snackbar 등을 사용하여 사용자에게 메시지를 보여줄 수 있습니다.
        Log.e("MedicalActivity", message) // 로그에 에러 메시지 출력
        // 예: Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
