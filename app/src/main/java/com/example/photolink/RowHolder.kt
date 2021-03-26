package com.example.photolink

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.photolink.Model.Place
import com.example.photolink.Model.Row
import kotlinx.android.synthetic.main.item_row.view.*

class RowHolder(private val itemView: View): RecyclerView.ViewHolder(itemView) {

    private var id: Int? = null

    fun bind(row: Row) {
        id = row.id
        itemView.name_row.text = row.name
        itemView.text_row.text = row.text
        itemView.checkbox_row.isChecked = row.checkImage
    }

    fun setListner(rowInteractor: RowInteractor?) {
        itemView.setOnClickListener {
            if (id != null) {
                rowInteractor?.onClickRow(id!!)
            }
        }
    }
}