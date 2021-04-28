package com.example.photolink

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.photolink.Model.IteamPlace

class PlaceAdapter : RecyclerView.Adapter<PlaceHolder>() {

    private var placeList: List<IteamPlace> = ArrayList()
    private var placeInteractor: PlaceInteractor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
        return PlaceHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false))
    }

    override fun onBindViewHolder(holder: PlaceHolder, position: Int) {
        holder.bind(placeList[position])
        holder.setListner(placeInteractor)
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    fun refreshPlaces(places: List<IteamPlace>){
        this.placeList = places
        notifyDataSetChanged()
    }

    fun setOnClickListener(placeInteractor: PlaceInteractor?){
        this.placeInteractor = placeInteractor
    }
}