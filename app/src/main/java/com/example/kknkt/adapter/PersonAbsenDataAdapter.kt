package com.example.kknkt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kknkt.R
import com.example.kknkt.models.PersonAbsenData
import kotlinx.android.synthetic.main.item_row_absening.view.*

class PersonAbsenDataAdapter: RecyclerView.Adapter<PersonAbsenDataAdapter.PersonAbsenDataViewHolder>(){
    inner class PersonAbsenDataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<PersonAbsenData>() {
        override fun areItemsTheSame(oldItem: PersonAbsenData, newItem: PersonAbsenData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PersonAbsenData, newItem: PersonAbsenData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonAbsenDataViewHolder {
        return PersonAbsenDataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row_person_absen_data,parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PersonAbsenDataViewHolder, position: Int) {
        val personAbsenData = differ.currentList[position]
        holder.itemView.apply {
            tvPersonName.text = personAbsenData.name
            tvAddress.text = personAbsenData.address
            setOnClickListener {
                onAbsenningClickListener?.let { it(PersonAbsenData()) }
            }
        }
    }

    var onAbsenningClickListener: ((PersonAbsenData)->Unit)? = null

    fun setOnClickListener(listener: (PersonAbsenData)->Unit){
        onAbsenningClickListener = listener
    }
}