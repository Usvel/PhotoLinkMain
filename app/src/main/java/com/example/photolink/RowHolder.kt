package com.example.photolink

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.photolink.Model.IteamPlace
import kotlinx.android.synthetic.main.item_row.view.*

class RowHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var name: String? = null

    fun bind(row: IteamPlace) {

        name = row.name

        itemView.name_row.text = row.name
        itemView.text_row.text = row.description

    }

    fun setListner(rowInteractor: RowInteractor?) {
        itemView.setOnClickListener {
            if (name != null) {
                rowInteractor?.onClickRow(name!!)
            }
        }
    }
}