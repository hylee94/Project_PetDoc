package com.example.project_petdoc.pets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_petdoc.databinding.ItemPetBinding
import com.example.project_petdoc.dataclass.Pet

class PetAdapter(var petList: List<Pet>) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {
    private var onItemEditClickListener: ((Pet) -> Unit)? = null

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
}