package com.example.photolink

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photolink.Model.IteamPlace
import kotlinx.android.synthetic.main.fragment_place.*
import kotlinx.android.synthetic.main.fragment_place.view.*

class PlaceFragment : Fragment() {

    val mainViewModel: MainLiveDate by lazy {
        ViewModelProvider(requireActivity()).get(MainLiveDate::class.java)
    }

    private var list: List<IteamPlace>? = null
    private var name: String? = null

    private var placeInteractor: PlaceInteractor? = null

    companion object {
        private const val ARG_MESSAGE_PLACE = "listPlace"
        private const val ARG_MESSAGE_PLACE_NAME = "namePlace"
        fun newInstance(list: ArrayList<IteamPlace>, name: String): PlaceFragment {
            val fragment = PlaceFragment()
            val arguments = Bundle()
            arguments.putParcelableArrayList(ARG_MESSAGE_PLACE, list)
            arguments.putString(ARG_MESSAGE_PLACE_NAME, name)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PlaceInteractor) {
            placeInteractor = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name.let {
            if (it != null) {
                (activity as AppCompatActivity?)!!.supportActionBar?.title = name
            }
        }


        val adapter = PlaceAdapter()
        adapter.setOnClickListener(placeInteractor)
        if (mainViewModel.baseURI.value != null) {
            adapter.baseUrl = mainViewModel.baseURI.value!!
        }
        view.recycler_place.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(view.context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(view.context.getDrawable(R.drawable.divider_drawable)!!)
        view.recycler_place.addItemDecoration(dividerItemDecoration)
//        placeViewModel.placeList.observe(viewLifecycleOwner, {
//            it?.let {
//                adapter.refreshPlaces(it)
//            }
//        })
        list.let {
            if (it != null) {
                adapter.refreshPlaces(it)
            }
        }



        view.fragment_refresh_place.setOnRefreshListener {
            placeInteractor?.onRefreshPlace()
            (view.recycler_place.layoutManager as LinearLayoutManager).scrollToPosition(0)
            view.fragment_refresh_place.isRefreshing = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_place, container, false)
        list = requireArguments().getParcelableArrayList(ARG_MESSAGE_PLACE)
        name = requireArguments().getString(ARG_MESSAGE_PLACE_NAME)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler_place.adapter = null
    }

    override fun onDetach() {
        super.onDetach()
        placeInteractor = null
    }
}