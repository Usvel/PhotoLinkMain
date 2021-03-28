package com.example.photolink.Model

import android.graphics.Bitmap

data class Row(
        var id: Int,
        var name: String,
        var text: String,
        var checkImage: Boolean,
        var imageBitmap: Bitmap?
)
