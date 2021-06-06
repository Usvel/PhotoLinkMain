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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photolink.Model.IteamPlace


class RowFragment : Fragment() {

    val mainViewModel: MainLiveDate by lazy {
        ViewModelProvider(requireActivity()).get(MainLiveDate::class.java)
    }

    private var list: List<IteamPlace>? = null
    private var name: String? = null
    private var rowInteractor: RowInteractor? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RowInteractor) {
            rowInteractor = context
        }
    }

    companion object {
        private const val ARG_MESSAGE_ROW = "listRow"
        private const val ARG_MESSAGE_ROW_NAME = "nameROW"
        fun newInstance(list: ArrayList<IteamPlace>, name: String): RowFragment {
            val fragment = RowFragment()
            val arguments = Bundle()
            arguments.putParcelableArrayList(ARG_MESSAGE_ROW, list)
            arguments.putString(ARG_MESSAGE_ROW_NAME, name)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inflate the layout for this fragment
        name.let {
            if (it != null) {
                (activity as AppCompatActivity?)!!.supportActionBar?.title = name
            }
        }

        val adapter = RowAdapter()
        adapter.setOnClickListener(rowInteractor)
        if (mainViewModel.baseURI.value != null){
            adapter.baseUrl = mainViewModel.baseURI.value!!
        }
        view.recycler_row.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(view.context, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(view.context.getDrawable(R.drawable.divider_drawable)!!)
        view.recycler_row.addItemDecoration(dividerItemDecoration)

        list.let {
            if (it != null) {
                adapter.refreshRow(it)
            }
        }

        view.fragment_refresh_row.setOnRefreshListener {
            rowInteractor?.onRefreshRow()
            (view.recycler_row.layoutManager as LinearLayoutManager).scrollToPosition(0)
            view.fragment_refresh_row.isRefreshing = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_row, container, false)
        list = requireArguments().getParcelableArrayList(ARG_MESSAGE_ROW)
        name = requireArguments().getString(ARG_MESSAGE_ROW_NAME)
        return view
    }
}