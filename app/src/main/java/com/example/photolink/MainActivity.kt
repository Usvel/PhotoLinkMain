package com.example.photolink

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.photolink.Model.Row

class MainActivity : AppCompatActivity(), PlaceInteractor, RowInteractor {

    private val mainFragment = PlaceFragment()
    private var rowFragment = RowFragment()

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

    override fun onClickRow(id: Int) {
        val new: List<Row> = PlaceData.getRow()
        for (item in new) {
            if (item.id == id) {
                item.checkImage = true
            }
        }
        rowFragment.rowViewModel.updateListPlace(new)
    }
}