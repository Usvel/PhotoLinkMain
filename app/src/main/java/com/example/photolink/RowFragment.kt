package com.example.photolink

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_row.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager


class RowFragment : Fragment() {

    val rowViewModel by lazy { ViewModelProviders.of(requireActivity()).get(RowViewModel::class.java) }

    private var rowInteractor: RowInteractor? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RowInteractor) {
            rowInteractor = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_row, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar?.title =
                view.context.getString(R.string.second_fragment)
        val adapter = RowAdapter()
        adapter.setOnClickListener(rowInteractor)
        view.recycler_row.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(view.context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(view.context.getDrawable(R.drawable.divider_drawable)!!)
        view.recycler_row.addItemDecoration(dividerItemDecoration)
        rowViewModel.placeList.observe(viewLifecycleOwner, {
            it?.let {
                adapter.refreshRow(it)
            }
        })
        view.fragment_refresh_row.setOnRefreshListener {
            rowInteractor?.onRefreshRow()
            (view.recycler_row.layoutManager as LinearLayoutManager).scrollToPosition(0)
            view.fragment_refresh_row.isRefreshing = false
        }
        return view
    }
}