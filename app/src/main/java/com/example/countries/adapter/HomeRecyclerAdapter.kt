package com.example.countries.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.databinding.HomeRecyclerRowBinding
import com.example.countries.model.Data
import com.example.countries.model.RoomModel

class HomeRecyclerAdapter(private val savedCountries : List<RoomModel>,
                          val listener : MyOnClickListener, val list: SaveListener) :
    RecyclerView.Adapter<HomeRecyclerAdapter.RowHolder>() {

    inner class RowHolder(val binding: HomeRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            // listener for checkbox in each row
            val button = binding.saveButton
            button.setOnCheckedChangeListener { but, isChecked ->
                val pos = absoluteAdapterPosition
                but.setOnClickListener {
                    list.click(pos,isChecked, but as CheckBox)
                }
            }
            // on click listener for each row
            itemView.setOnClickListener {
                val pos = absoluteAdapterPosition
                listener.onClick(pos,button)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.code == newItem.code
        }
        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)


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
        holder.binding.countryName.text = differ.currentList[position].name

        // To check checkbox for already saved countries, comparing with database
        println(savedCountries.size)
        for(ct in savedCountries){
            if(differ.currentList[position].code == ct.code){
                holder.binding.saveButton.isChecked = true
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.count()
    }
}