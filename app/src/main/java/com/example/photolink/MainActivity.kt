package com.example.photolink

import android.app.usage.UsageEvents
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.photolink.Model.IteamPlace
import com.example.photolink.api.RequestApiImpl
import com.example.photolink.api.RequestRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import java.io.File

class MainActivity : AppCompatActivity(), PlaceInteractor, RowInteractor, CameraInteractor, DescriptionInteractor {

    private val newsRepository by lazy(UsageEvents.Event.NONE) { RequestRepository(RequestApiImpl(this)) }
    private val compositeDisposable = CompositeDisposable()

    val mainViewModel: MainLiveDate by lazy {
        ViewModelProvider(this).get(MainLiveDate::class.java)
    }

    //  private val fragmentSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makeCurrentFragment(mainFragment)
        loadPlace()
    }


    fun loadPlace() {
        val disposable = newsRepository.loadListGson().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ requst ->
                    Log.d("RX", requst.toString())
                    val startFragment = PlaceFragment.newInstance(requst as ArrayList<IteamPlace>, getString(R.string.main_fragment))
                    makeCurrentFragment(startFragment)
                }, {
                    AlertDialog.Builder(this).setMessage("Ошибка загрузки").setMessage(it.message).show()
                    val startFragment = PlaceFragment.newInstance(arrayListOf(), getString(R.string.main_fragment))
                    makeCurrentFragment(startFragment)
                }
                )//.updateListPost(news as MutableList<Post>)}
        compositeDisposable.add(disposable)
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.nav_frame, fragment)
            commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("Place", mainViewModel.lastName.value.toString())
        mainViewModel.removePlace()
        Log.d("Place", mainViewModel.namePalace.value)
    }

    override fun onClickPlace(list: List<IteamPlace>, type: Boolean, name: String) {
        if (type) {
            val fragment = RowFragment.newInstance(list as ArrayList<IteamPlace>, name)
            supportFragmentManager.beginTransaction().replace(R.id.nav_frame, fragment).addToBackStack(null).commit()
        } else {
            val fragment = PlaceFragment.newInstance(list as ArrayList<IteamPlace>, name)
            supportFragmentManager.beginTransaction().replace(R.id.nav_frame, fragment).addToBackStack(null).commit()
        }
        mainViewModel.addPlace(name)
    }

    override fun onRefreshPlace() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        loadPlace()
        Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show()
        mainViewModel.startMain()
    }

    override fun onClickRow(name: String) {
        supportFragmentManager.beginTransaction().replace(R.id.nav_frame, CameraFragment()).addToBackStack(null).commit()
        mainViewModel.addPlace(name)
    }

    override fun onRefreshRow() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        loadPlace()
        Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show()
        mainViewModel.startMain()
    }

    override fun onSavePhoto(description: String) {
        val file = mainViewModel.listURI.value?.last()!!.toFile()
        val arrayList = ArrayList<File>()
        arrayList.add(file)
        Log.d("PlaceOut", mainViewModel.namePalace.value)
        val disposable = newsRepository.saveFile(mainViewModel.namePalace.value!!, description, arrayList).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val str = it.string()
                    Log.d("Body", str)
                    if (str.contains("file uploaded successfully")) {
                        Toast.makeText(this, "Фото успешно загруженны", Toast.LENGTH_SHORT).show()
                    } else {
                        AlertDialog.Builder(this).setTitle("Ошибка закгрузки").setMessage(str).show()
                    }
                }, {
                    AlertDialog.Builder(this).setTitle("Ошибка закгрузки").setMessage(it.message).show()
                })
        compositeDisposable.add(disposable)
        onBackPressed()
    }

    override fun onOpenDescription(fileUri: Uri) {
        mainViewModel.listURI.value = ArrayList()
        mainViewModel.listURI.value?.add(fileUri)
        supportFragmentManager.beginTransaction().replace(R.id.nav_frame, DescriptionFragment()).addToBackStack(null).commit()

    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}