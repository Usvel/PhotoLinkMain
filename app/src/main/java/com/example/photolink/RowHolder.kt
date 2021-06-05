package com.example.photolink

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.photolink.Model.IteamPlace
import com.example.photolink.api.RequestApiImpl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_place.view.*
import kotlinx.android.synthetic.main.item_row.view.*

class RowHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var name: String? = null

    fun bind(row: IteamPlace) {

        name = row.name

        itemView.name_row.text = row.name
        itemView.text_row.text = row.description

        if (row.urlImage == null) {
            itemView.image_row.setImageResource(R.drawable.ic_launcher_foreground)
        } else {
            Picasso.with(itemView.context).load(RequestApiImpl.BASE_URL + row.urlImage).fit().centerCrop().into(itemView.image_row)
        }

    }

    fun setListner(rowInteractor: RowInteractor?) {
        itemView.setOnClickListener {
            if (name != null) {
                rowInteractor?.onClickRow(name!!)
            }
        }
    }
}