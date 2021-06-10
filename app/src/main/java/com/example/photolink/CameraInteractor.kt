package com.example.photolink

import android.net.Uri


interface CameraInteractor {
    fun onOpenDescription(file: Uri)
    fun onBack()
}