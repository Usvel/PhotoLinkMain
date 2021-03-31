package com.example.photolink.Model

data class Request (
        val ispoccess: Boolean,
        val listSite : List<Site>
        )

data class Site (
        val id : Int,
        val name : String,
        val description: String,
        val checkImage : Boolean,
        val urlImage : String
        )