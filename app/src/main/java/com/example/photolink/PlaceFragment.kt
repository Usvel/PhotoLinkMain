package com.example.photolink

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_place.*
import kotlinx.android.synthetic.main.fragment_place.view.*

class PlaceFragment : Fragment() {

    val placeViewModel by lazy { ViewModelProviders.of(requireActivity()).get(PlaceViewModel::class.java) }

    private var placeInteractor: PlaceInteractor? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PlaceInteractor) {
            placeInteractor = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar?.title =
                view.context.getString(R.string.main_fragment)
        val adapter = PlaceAdapter()
        adapter.setOnClickListener(placeInteractor)
        view.recycler_place.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(view.context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(view.context.getDrawable(R.drawable.divider_drawable)!!)
        view.recycler_place.addItemDecoration(dividerItemDecoration)
        placeViewModel.placeList.observe(viewLifecycleOwner, {
            it?.let {
                adapter.refreshPlaces(it)
            }
        })
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