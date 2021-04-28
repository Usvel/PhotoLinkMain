package com.example.photolink.Model

import android.os.Parcel
import android.os.Parcelable


data class IteamPlace(
        var ispoccess: Boolean?,
        var name: String?,
        var urlImage: String?,
        var description: String?,
        var listSite: List<IteamPlace>?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(CREATOR)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(ispoccess)
        parcel.writeString(name)
        parcel.writeString(urlImage)
        parcel.writeString(description)
        parcel.writeTypedList(listSite)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IteamPlace> {
        override fun createFromParcel(parcel: Parcel): IteamPlace {
            return IteamPlace(parcel)
        }

        override fun newArray(size: Int): Array<IteamPlace?> {
            return arrayOfNulls(size)
        }
    }
}