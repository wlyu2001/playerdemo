package com.shishiapp.playerdemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shishiapp.playerdemo.R
import com.shishiapp.playerdemo.adapter.ContentListAdapter
import com.shishiapp.playerdemo.databinding.FragmentContentListBinding
import com.shishiapp.playerdemo.viewmodel.ContentListViewModel

class ContentListFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentContentListBinding
    private lateinit var adapter: ContentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewDataBinding = FragmentContentListBinding.inflate(inflater, container, false).apply {

            viewmodel =
                ViewModelProvider(this@ContentListFragment).get(ContentListViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            arguments?.let { viewModel.fetchContentList(it.getInt("key")) }

            viewModel.contentListLive.observe(viewLifecycleOwner, {
                adapter.updateContentList(it)
            })

            adapter = ContentListAdapter()
            val layoutManager = LinearLayoutManager(activity)
            val contentListRecyclerView =
                view.findViewById<RecyclerView>(R.id.recyclerview_content_list)
            contentListRecyclerView.layoutManager = layoutManager
            contentListRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    activity,
                    layoutManager.orientation
                )
            )
            contentListRecyclerView.adapter = adapter
        }
    }
}