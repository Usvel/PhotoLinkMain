package com.example.photolink

import android.app.usage.UsageEvents
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.photolink.Model.IteamPlace
import com.example.photolink.api.RequestApiImpl
import com.example.photolink.api.RequestRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.FileInputStream


class MainActivity : AppCompatActivity(), PlaceInteractor, RowInteractor, CameraInteractor, DescriptionInteractor, ServerSettingsInteractor {

    companion object {
        private val FILE_NAME = "content.txt"
    }

    private val newsRepository by lazy(UsageEvents.Event.NONE) { RequestRepository(RequestApiImpl(this)) }
    private val compositeDisposable = CompositeDisposable()

    val mainViewModel: MainLiveDate by lazy {
        ViewModelProvider(this).get(MainLiveDate::class.java)
    }

    //  private val fragmentSize: Int = 0
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the main_menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.settingsURI -> {
                val fragment = ServerSettings()
                supportFragmentManager.beginTransaction().replace(R.id.nav_frame, fragment).addToBackStack(null).commit()
            }

            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makeCurrentFragment(mainFragment)
        if (openText() != null) {
            mainViewModel.setBaseURI(openText()!!)
        }
        mainViewModel.baseURI.observe(this, Observer {
            saveText(it)
            newsRepository.updateURI(it)
            loadPlace()
        })
    }


    fun loadPlace() {
        val disposable = newsRepository.loadListGson().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ requst ->
                    Log.d("RX", requst.toString())
                    val startFragment = PlaceFragment.newInstance(requst as ArrayList<IteamPlace>, getString(R.string.main_fragment))
                    mainViewModel.startMain()
                    makeCurrentFragment(startFragment)
                }, {
                    AlertDialog.Builder(this).setMessage("Ошибка загрузки").setMessage(it.message).show()

                }
                )//.updateListPost(news as MutableList<Post>)}
        compositeDisposable.add(disposable)
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_frame, fragment)
            commit()
        }
    }

    override fun onBackPressed() {
        var flag = true
        if (supportFragmentManager.fragments.last() is ServerSettings) {
            flag = false
        }
        super.onBackPressed()
        if (flag) {
            Log.d("Place-valueList", mainViewModel.lastName.value.toString())
            if (!supportFragmentManager.fragments.isEmpty()) {
                if (!(supportFragmentManager.fragments.last() is CameraFragment)) {
                    mainViewModel.removePlace()
                }
            }
            Log.d("Place-value", mainViewModel.namePalace.value)
        }
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
                        Toast.makeText(this, "Фото успешно загруженно", Toast.LENGTH_SHORT).show()
                    } else {
                        AlertDialog.Builder(this).setTitle("Ошибка закгрузки").setMessage(str).show()
                    }
                    file.delete()
                }, {
                    AlertDialog.Builder(this).setTitle("Ошибка закгрузки").setMessage(it.message).show()
                    file.delete()
                })
        compositeDisposable.add(disposable)
        onBackPressed()
    }

    override fun onOpenDescription(fileUri: Uri) {
        mainViewModel.listURI.value?.forEach {
            it.toFile().delete()
        }
        mainViewModel.listURI.value = ArrayList()
        mainViewModel.listURI.value?.add(fileUri)
        supportFragmentManager.beginTransaction().replace(R.id.nav_frame, DescriptionFragment()).addToBackStack(null).commit()

    }

    override fun onStop() {
        mainViewModel.listURI.value?.forEach {
            it.toFile().delete()
        }
        compositeDisposable.clear()
        super.onStop()
    }

    override fun closeServerSettings() {
        onBackPressed()
    }

    // открытие файла
    fun openText():String? {
        var fin: FileInputStream? = null
        var text: String? = null
        try {
            fin = openFileInput(FILE_NAME)
            val bytes = ByteArray(fin.available())
            fin.read(bytes)
            text = String(bytes)
        } catch (ex: IOException) {
            Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
        } finally {
            try {
                if (fin != null) fin.close()
            } catch (ex: IOException) {
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        return text
    }

    // сохранение файла
    fun saveText(text: String) {
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE)
            fos.write(text.toByteArray())
        } catch (ex: IOException) {
            Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
        } finally {
            try {
                if (fos != null) fos.close()
            } catch (ex: IOException) {
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}