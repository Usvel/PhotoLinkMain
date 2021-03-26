package com.example.photolink

import com.example.photolink.Model.Place
import com.example.photolink.Model.Row

object PlaceData {

    fun getPlace() = listOf(
            Place(1, "Город 1", "https://cdnimg.rg.ru/img/content/173/40/05/iStock-1124383135_d_850.jpg"),
            Place(2, "Город 2", "https://st.depositphotos.com/1007930/2454/i/600/depositphotos_24543229-stock-photo-hong-kong.jpg"),
            Place(3, "Город 3", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTVXh6qrI_ZDsX7IiXYeu2YaOaVhdaZuMgRFA&usqp=CAU")
    )

    fun getRow() = listOf(
            Row(1, "Улица 1", "Описание 2", true),
            Row(2, "Улица 2", "Описание 2", false),
            Row(3, "Улица 3", "Описание 3", false),
    )
}