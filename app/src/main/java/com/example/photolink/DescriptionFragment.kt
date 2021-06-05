package com.example.photolink

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.photolink.Model.IteamPlace
import kotlinx.android.synthetic.main.fragment_description.*
import java.io.File
import java.net.URI

class DescriptionFragment : Fragment() {

    private var descriptionInteractor: DescriptionInteractor? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DescriptionInteractor) {
            descriptionInteractor = context
        }
    }

    val mainViewModel: MainLiveDate by lazy {
        ViewModelProvider(requireActivity()).get(MainLiveDate::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_description, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)
        mainViewModel.listURI.observe(viewLifecycleOwner, Observer {
            descriptionImage.setImageURI(it.last())
        })
        descriptionBtnOk.setOnClickListener {
            descriptionInteractor?.onSavePhoto(descriptionEditName.text.toString())
        }
    }
}