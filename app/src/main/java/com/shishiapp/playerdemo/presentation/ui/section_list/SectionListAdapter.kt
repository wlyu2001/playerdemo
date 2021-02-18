package com.shishiapp.playerdemo.presentation.ui.section_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shishiapp.playerdemo.databinding.ViewSectionListItemBinding
import com.shishiapp.playerdemo.network.model.Section

class SectionListAdapter : RecyclerView.Adapter<SectionListViewHolder>() {

    var sectionList: List<Section> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ViewSectionListItemBinding.inflate(inflater, parent, false)
        return SectionListViewHolder(dataBinding)
    }

    override fun getItemCount() = sectionList.size

    override fun onBindViewHolder(holder: SectionListViewHolder, position: Int) {
        holder.setup(sectionList[position])
    }

    fun updateSectionList(sectionList: List<Section>) {
        this.sectionList = sectionList
        notifyDataSetChanged()
    }
}