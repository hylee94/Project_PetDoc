package com.example.project_petdoc.pets

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_petdoc.databinding.ItemPetBinding
import com.example.project_petdoc.dataclass.Pet

class PetAdapter(
    var petList: MutableList<Pet>,
    private val itemClick: (Pet) -> Unit // 클릭 리스너 매개변수 추가
) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {
    private var onItemEditClickListener: ((Pet) -> Unit)? = null

    var onDeleteClick: ((Int) -> Unit)? = null

    inner class PetViewHolder(private val binding: ItemPetBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pet: Pet) {
            binding.textViewType.text = pet.type
            binding.textViewName.text = pet.name
            binding.textViewGender.text = pet.gender
            binding.textViewAge.text = pet.age.toString()
            binding.textViewHospital.text = pet.hospital

            binding.buttonEdit.setOnClickListener {
                onItemEditClickListener?.invoke(pet)
            }

            // 삭제 버튼 클릭 이벤트 설정
            binding.buttonDelete.setOnClickListener {
                onDeleteClick?.invoke(adapterPosition) // 클릭 시 현재 포지션 전달
            }

            // 아이템 클릭 이벤트 설정
            binding.root.setOnClickListener {
                itemClick(pet) // 클릭 시 애완동물 데이터 전달
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val binding = ItemPetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        holder.bind(petList[position])
    }

    override fun getItemCount(): Int = petList.size

    fun setOnItemEditClickListener(listener: (Pet) -> Unit) {
        onItemEditClickListener = listener
    }

    fun removeAt(position: Int) {
        petList.removeAt(position) // 아이템 삭제
        notifyItemRemoved(position) // UI 업데이트
    }
}
