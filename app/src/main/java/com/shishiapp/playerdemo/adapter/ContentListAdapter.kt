package com.shishiapp.playerdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shishiapp.playerdemo.databinding.ViewContentListItemBinding
import com.shishiapp.playerdemo.model.Content
import com.shishiapp.playerdemo.view.ContentListViewHolder

class ContentListAdapter : RecyclerView.Adapter<ContentListViewHolder>() {

    var contentList: List<Content> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ViewContentListItemBinding.inflate(inflater, parent, false)
        return ContentListViewHolder(dataBinding)
    }

    override fun getItemCount() = contentList.size

    override fun onBindViewHolder(holder: ContentListViewHolder, position: Int) {
        holder.setup(contentList[position])
    }

    fun updateContentList(contentList: List<Content>) {
        this.contentList = contentList
        notifyDataSetChanged()
    }
}