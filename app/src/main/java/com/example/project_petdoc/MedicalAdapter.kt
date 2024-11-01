package com.example.project_petdoc

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_petdoc.databinding.ItemMedicalBinding
import com.example.project_petdoc.dataclass.Record

class MedicalAdapter(
    private val medicalList: MutableList<Record>, // Medical 대신 Record 사용
    private val onItemClick: (Record) -> Unit // 클릭 이벤트 전달
) : RecyclerView.Adapter<MedicalAdapter.MedicalViewHolder>() {

    // ViewHolder 정의
    class MedicalViewHolder(val binding: ItemMedicalBinding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalViewHolder {
        val binding = ItemMedicalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MedicalViewHolder(binding)
    }

    // 데이터와 View를 연결
    override fun onBindViewHolder(holder: MedicalViewHolder, position: Int) {
        val record = medicalList[position]
        holder.binding.diseaseText.text = record.disease
        holder.binding.dateText.text = record.date
        holder.binding.opinionText.text = record.doctor_op

        // 항목 클릭 시 상세 보기 화면으로 이동
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, RecordActivity2::class.java).apply {
                putExtra("date", record.date)
                putExtra("disease", record.disease)
                putExtra("opinion", record.doctor_op)
                putExtra("prescription", record.medicine) // 처방 정보
                putExtra("fee", record.fee) // 병원비 정보
                putExtra("hospital", record.hospital) // 병원명 정보
                putExtra("memo", record.memo) // 메모 정보
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    // 아이템 개수 반환
    override fun getItemCount(): Int = medicalList.size
}