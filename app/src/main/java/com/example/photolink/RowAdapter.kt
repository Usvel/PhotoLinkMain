package com.example.photolink

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.photolink.Model.IteamPlace

class RowAdapter: RecyclerView.Adapter<RowHolder>() {

    private var rowList: List<IteamPlace> = ArrayList()
    private var rowInteractor: RowInteractor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        return RowHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false))
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.bind(rowList[position])
        holder.setListner(rowInteractor)
    }

    override fun getItemCount(): Int {
        return rowList.size
    }

    fun setOnClickListener(rowInteractor: RowInteractor?){
        this.rowInteractor = rowInteractor
    }

    fun refreshRow(row: List<IteamPlace>){
        this.rowList = row
        notifyDataSetChanged()
    }
}