package com.example.photolink

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_server_settings.*

class ServerSettings : Fragment() {

    val mainViewModel: MainLiveDate by lazy {
        ViewModelProvider(requireActivity()).get(MainLiveDate::class.java)
    }

    private var serverSettingsInteractor: ServerSettingsInteractor? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ServerSettingsInteractor) {
            serverSettingsInteractor = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.baseURI.observe(viewLifecycleOwner, {
            it.let {
                baseEditText.setText(it)
            }
        })
        settingsBtn.setOnClickListener {
            if (!baseEditText.text.isEmpty()) {
                mainViewModel.setBaseURI(baseEditText.text.toString())
                serverSettingsInteractor?.closeServerSettings()
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_server_settings, container, false)
    }
}