package com.example.photolink

import android.Manifest
import android.app.Activity
import android.app.usage.UsageEvents
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.photolink.Model.Place
import com.example.photolink.Model.Row
import com.example.photolink.api.RequestApiImpl
import com.example.photolink.api.RequestRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), PlaceInteractor, RowInteractor {

    companion object {
        private const val REQUEST_CODE = 420
        private const val PHOTO_PERMISSIONS_REQUEST_CODE = 2
    }

    val viewModelNews: PlaceViewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    private val newsRepository by lazy(UsageEvents.Event.NONE) { RequestRepository(RequestApiImpl(this)) }

    private var idRow: Int? = null

    lateinit var currentPhotoPath: String

    private val mainFragment = PlaceFragment()
    private var rowFragment = RowFragment()

    //  private val fragmentSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        makeCurrentFragment(mainFragment)
        loadPlace()
    }

    fun loadPlace() {
        newsRepository.loadListPlace().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { requst ->
                    val mutableList = mutableListOf<Place>()
                    for (i in requst.listSite) {
                        val place = Place(
                                i.id,
                                i.name,
                                i.urlImage
                        )
                        mutableList.add(place)
                    }
                    viewModelNews.setListPlace(mutableList)
                }//.updateListPost(news as MutableList<Post>)}
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
        //mainFragment.placeViewModel.updateListPlace()
        loadPlace()
    }

    override fun onClickRow(id: Int) {
        idRow = id
        supportFragmentManager.beginTransaction().replace(R.id.nav_frame, CameraFragment()).addToBackStack(null).commit()

    }

    override fun onRefreshRow() {
        val new: List<Row> = PlaceData.getRow()
        rowFragment.rowViewModel.updateListPlace(new)
    }

}