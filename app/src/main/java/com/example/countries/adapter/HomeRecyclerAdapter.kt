package com.example.countries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.databinding.HomeRecyclerRowBinding
import com.example.countries.model.Data
import com.example.countries.model.RoomModel

class HomeRecyclerAdapter(private val countryList : List<Data>, private val savedCountries : List<RoomModel>,
                          val listener : MyOnClickListener, val list: SaveListener) :
    RecyclerView.Adapter<HomeRecyclerAdapter.RowHolder>() {

    inner class RowHolder(val binding: HomeRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            val button = binding.saveButton
            button.setOnCheckedChangeListener { button, isChecked ->
                val pos = absoluteAdapterPosition
                button.setOnClickListener {
                    list.click(pos,isChecked, button as CheckBox)
                }
            }

            itemView.setOnClickListener {
                val pos = absoluteAdapterPosition
                listener.onClick(pos,button)
            }
        }
    }

    interface MyOnClickListener {
        fun onClick(position : Int,item: CheckBox)
    }
    interface SaveListener {
        fun click(pos : Int,isChecked : Boolean,item: CheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val binding = HomeRecyclerRowBinding.inflate(LayoutInflater.from(parent.context))
        return RowHolder(binding)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.binding.countryName.text = countryList[position].name
        for(ct in savedCountries){
            if(countryList[position].code == ct.code){
                holder.binding.saveButton.isChecked = true
            }
        }

    }

    override fun getItemCount(): Int {
        return countryList.count()
    }


}