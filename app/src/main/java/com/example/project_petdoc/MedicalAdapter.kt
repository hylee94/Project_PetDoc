import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_petdoc.databinding.ItemMedicalBinding
import com.example.teamproject.Medical
import com.example.teamproject.RecordActivity2

class MedicalAdapter(
    private val medicalList: List<Medical>,
    private val onItemClick: (Medical) -> Unit // 클릭 이벤트 전달
) : RecyclerView.Adapter<MedicalAdapter.MedicalViewHolder>() {

    // ViewHolder 정의
    class MedicalViewHolder(val binding: ItemMedicalBinding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalViewHolder {
        val binding = ItemMedicalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MedicalViewHolder(binding)
    }

    //데이터와 View를 연결
    override fun onBindViewHolder(holder: MedicalViewHolder, position: Int) {
        val medical = medicalList[position]
        holder.binding.diseaseText.text = medical.disease
        holder.binding.dateText.text = medical.date
        holder.binding.opinionText.text = medical.doctor_op

        // 항목 클릭 시 상세 보기 화면으로 이동
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, RecordActivity2::class.java).apply {
                putExtra("date", medical.date)
                putExtra("disease", medical.disease)
                putExtra("opinion", medical.doctor_op)
                putExtra("prescription", "처방 정보") // 필요시 추가
                putExtra("fee", "병원비 정보")
                putExtra("hospital", "병원명 정보")
                putExtra("memo", "메모 정보")
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    //아이템 개수 반환
    override fun getItemCount(): Int {
        return medicalList.size
    }
}

