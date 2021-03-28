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
            Row(1, "Улица 1", "Описание 2", true, null),
            Row(2, "Улица 2", "Описание 2", false, null),
            Row(3, "Улица 3", "Описание 3", false, null),
            Row(4, "Улица 4", "Описание 3", false, null),
            Row(5, "Улица 5", "Описание 3", false,null),
            Row(6, "Улица 6", "Описание 3", false,null),
            Row(7, "Улица 7", "Описание 3", false,null),
            Row(8, "Улица 8", "Описание 3", false,null),
            Row(9, "Улица 9", "Описание 3", false,null),
            Row(10, "Улица 10", "Описание 3", false,null),
            Row(11, "Улица 11", "Описание 3", false,null),
            Row(12, "Улица 12", "Описание 3", false,null),
            Row(13, "Улица 13", "Описание 3", false,null),
            Row(14, "Улица 14", "Описание 3", false,null),
            Row(15, "Улица 15", "Описание 3", false,null)

    )
}