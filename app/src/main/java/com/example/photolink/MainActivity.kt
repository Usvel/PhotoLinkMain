package com.example.photolink

import android.app.usage.UsageEvents
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.photolink.Model.IteamPlace
import com.example.photolink.api.RequestApiImpl
import com.example.photolink.api.RequestRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), PlaceInteractor, RowInteractor {

    private val newsRepository by lazy(UsageEvents.Event.NONE) { RequestRepository(RequestApiImpl(this)) }

    //  private val fragmentSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makeCurrentFragment(mainFragment)
        loadPlace()
    }

    fun loadPlace() {
        newsRepository.loadListPlace().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { requst ->
                    Log.d("RX", requst.toString())
                    val startFragment = PlaceFragment.newInstance(requst as ArrayList<IteamPlace>, getString(R.string.main_fragment))
                    makeCurrentFragment(startFragment)
                }//.updateListPost(news as MutableList<Post>)}
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_frame, fragment)
            commit()
        }
    }

    override fun onClickPlace(list: List<IteamPlace>, type: Boolean, name: String) {
        if (type) {
            val fragment = RowFragment.newInstance(list as ArrayList<IteamPlace>,name)
            supportFragmentManager.beginTransaction().replace(R.id.nav_frame, fragment).addToBackStack(null).commit()
        } else {
            val fragment = PlaceFragment.newInstance(list as ArrayList<IteamPlace>, name)
            supportFragmentManager.beginTransaction().replace(R.id.nav_frame, fragment).addToBackStack(null).commit()
        }
    }

    override fun onRefreshPlace() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        loadPlace()
        Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show()
    }

    override fun onClickRow(name: String) {
        supportFragmentManager.beginTransaction().replace(R.id.nav_frame, CameraFragment()).addToBackStack(null).commit()
    }

    override fun onRefreshRow() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        loadPlace()
        Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show()
    }

}