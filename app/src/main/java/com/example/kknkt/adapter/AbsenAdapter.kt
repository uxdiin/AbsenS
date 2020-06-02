package com.example.kknkt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kknkt.R
import com.example.kknkt.models.Absen
import kotlinx.android.synthetic.main.item_row_absen.view.*

class AbsenAdapter: RecyclerView.Adapter<AbsenAdapter.AbsenViewHolder>() {
    inner class AbsenViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Absen>() {
        override fun areItemsTheSame(oldItem: Absen, newItem: Absen): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Absen, newItem: Absen): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsenViewHolder {
        return AbsenViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row_absen,parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: AbsenViewHolder, position: Int) {
        val Absen = differ.currentList[position]
        holder.itemView.apply {
            tvEventName.text = Absen.eventName
            tvEventDate.text = Absen.date
            setOnClickListener {
                onAbsenClickListener?.let { it(Absen) }
            }
        }
    }

    var onAbsenClickListener: ((Absen)->Unit)? = null

    fun setOnClickListener(listener: (Absen)->Unit){
        onAbsenClickListener = listener
    }
}