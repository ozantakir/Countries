package com.example.countries.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R
import com.example.countries.databinding.HomeRecyclerRowBinding
import com.example.countries.model.Data

class HomeRecyclerAdapter(private val countryList : List<Data>, val listener : MyOnClickListener) :
    RecyclerView.Adapter<HomeRecyclerAdapter.RowHolder>() {

    inner class RowHolder(val binding: HomeRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                val pos = absoluteAdapterPosition
                listener.onClick(pos)
            }
        }
    }

    interface MyOnClickListener {
        fun onClick(position : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val binding = HomeRecyclerRowBinding.inflate(LayoutInflater.from(parent.context))
        return RowHolder(binding)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.binding.countryName.text = countryList[position].name
        holder.binding.saveButton.setImageResource(R.drawable.ic_star)
    }

    override fun getItemCount(): Int {
        return countryList.count()
    }


}