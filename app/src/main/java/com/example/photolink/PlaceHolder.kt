package com.example.photolink

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.photolink.Model.IteamPlace
import com.example.photolink.api.RequestApiImpl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_place.view.*

class PlaceHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var name: String? = null
    private var list: List<IteamPlace>? = null
    private var type: Boolean? = null

    fun bind(place: IteamPlace) {
        itemView.name_place.text = place.name
        list = place.listSite
        type = place.ispoccess
        name = place.name
        if (place.urlImage == null) {
            itemView.image_place.setImageResource(R.drawable.ic_launcher_foreground)
        } else {
            Picasso.with(itemView.context).load(RequestApiImpl.BASE_URL + place.urlImage).fit().centerCrop().into(itemView.image_place)
        }
    }

    fun setListner(placeInteractor: PlaceInteractor?) {
        itemView.setOnClickListener {
            if (list != null && type != null && name != null) {
                placeInteractor?.onClickPlace(list!!, type!!, name!!)
            }
        }
    }
}