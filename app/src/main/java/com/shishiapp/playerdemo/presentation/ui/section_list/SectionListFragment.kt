package com.shishiapp.playerdemo.presentation.ui.section_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shishiapp.playerdemo.R
import com.shishiapp.playerdemo.databinding.FragmentSectionListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SectionListFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentSectionListBinding
    private lateinit var adapter: SectionListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        val viewModel: SectionListViewModel by viewModels()

        viewDataBinding = FragmentSectionListBinding.inflate(inflater, container, false).apply {

            viewmodel =
                ViewModelProvider(this@SectionListFragment).get(SectionListViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            viewModel.fetchSectionList()

            viewModel.sectionListData.observe(viewLifecycleOwner, {
                adapter.updateSectionList(it)
            })

            adapter = SectionListAdapter()
            val layoutManager = LinearLayoutManager(activity)
            val sectionListRecyclerView =
                view.findViewById<RecyclerView>(R.id.recyclerview_section_list)
            sectionListRecyclerView.layoutManager = layoutManager
            sectionListRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    activity,
                    layoutManager.orientation
                )
            )
            sectionListRecyclerView.adapter = adapter
        }
    }
}