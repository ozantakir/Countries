package com.example.countries.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.databinding.DetailsRecyclerRowBinding
import com.example.countries.model.RoomModel

class SavedCountriesRecyclerAdapter(private val savedCountries : List<RoomModel>, val listener: MyOnClickListener, val list: DeleteListener) :
    RecyclerView.Adapter<SavedCountriesRecyclerAdapter.ItemHolder>() {

    inner class ItemHolder(val binding: DetailsRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            // on click listener for delete button in each row
            binding.deleteButton.setOnClickListener {
                val pos = absoluteAdapterPosition
                list.deleteClick(pos)
            }
            // on click listener for each row
            itemView.setOnClickListener {
                val pos = absoluteAdapterPosition
                listener.onClick(pos)
            }
        }
    }
    interface MyOnClickListener {
        fun onClick(position : Int)
    }
    interface DeleteListener {
        fun deleteClick(pos: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = DetailsRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.binding.countryName.text = savedCountries[position].name
    }

    override fun getItemCount(): Int {
        return savedCountries.size
    }
}