package com.example.photolink

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.photolink.Model.Row
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), PlaceInteractor, RowInteractor {

    companion object{
        private const val REQUEST_CODE = 420
    }

    private var idRow: Int? = null

    lateinit var currentPhotoPath: String

    private val mainFragment = PlaceFragment()
    private var rowFragment = RowFragment()

  //  private val fragmentSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        makeCurrentFragment(mainFragment)
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_frame, fragment)
            commit()
        }
    }

    override fun onClickPlace(id: Int) {
        supportFragmentManager.beginTransaction().replace(R.id.nav_frame, rowFragment).addToBackStack(null).commit()
    }

    override fun onRefreshPlace() {
        mainFragment.placeViewModel.updateListPlace()
    }

    override fun onClickRow(id: Int) {
        idRow = id
        dispatchTakePictureIntent()
    }

    override fun onRefreshRow() {
        val new: List<Row> = PlaceData.getRow()
        rowFragment.rowViewModel.updateListPlace(new)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takeImage = BitmapFactory.decodeFile(currentPhotoPath)
            val new: List<Row> = rowFragment.rowViewModel.placeList.value!!.toList()
                for (item in new) {
                    if (item.id == idRow) {
                        item.imageBitmap = takeImage
                        item.checkImage = true
                    }
                }
                rowFragment.rowViewModel.updateListPlace(new)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    //...
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.android.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_CODE)
                }
            }
        }
    }
}