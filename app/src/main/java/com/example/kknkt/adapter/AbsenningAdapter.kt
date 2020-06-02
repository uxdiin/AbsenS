package com.example.kknkt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kknkt.R
import com.example.kknkt.models.Person
import kotlinx.android.synthetic.main.item_row_absening.view.*

class AbsenningAdapter: RecyclerView.Adapter<AbsenningAdapter.AbsenningViewHolder>(){
    inner class AbsenningViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsenningViewHolder {
        return AbsenningViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row_absening,parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: AbsenningViewHolder, position: Int) {
        val person = differ.currentList[position]
        holder.itemView.apply {
            tvPersonName.text = person.name
            tvAddress.text = person.address
            btnAbsen.setOnClickListener {
                onAbsenningClickListener?.let { it(person) }
            }

        }
    }

    var onAbsenningClickListener: ((Person)->Unit)? = null

    fun setOnClickListener(listener: (Person)->Unit){
        onAbsenningClickListener = listener
    }
}