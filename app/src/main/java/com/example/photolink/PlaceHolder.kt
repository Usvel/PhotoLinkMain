package com.example.photolink

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.photolink.Model.Place
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_place.view.*

class PlaceHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var id: Int? = null

    fun bind(place: Place) {
        id = place.id
        itemView.name_place.text = place.name
        if (place.image == null) {
            itemView.image_place.setImageResource(R.drawable.ic_launcher_foreground)
        } else {
            Picasso.with(itemView.context).load(place.image).fit().centerCrop().into(itemView.image_place)
        }
    }

    fun setListner(placeInteractor: PlaceInteractor?) {
        itemView.setOnClickListener {
            if (id != null) {
                placeInteractor?.onClickPlace(id!!)
            }
        }
    }
}