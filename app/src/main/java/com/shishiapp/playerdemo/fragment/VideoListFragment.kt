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
import com.shishiapp.playerdemo.adapter.VideoListAdapter
import com.shishiapp.playerdemo.databinding.FragmentVideoListBinding
import com.shishiapp.playerdemo.viewmodel.VideoListViewModel

class VideoListFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentVideoListBinding
    private lateinit var adapter: VideoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewDataBinding = FragmentVideoListBinding.inflate(inflater, container, false).apply {

            viewmodel =
                ViewModelProvider(this@VideoListFragment).get(VideoListViewModel::class.java)
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
                adapter.updateVideoList(it)
            })

            adapter = VideoListAdapter()
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