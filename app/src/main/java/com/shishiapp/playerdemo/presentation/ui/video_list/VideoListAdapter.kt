package com.shishiapp.playerdemo.presentation.ui.video_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shishiapp.playerdemo.databinding.ViewVideoListItemBinding
import com.shishiapp.playerdemo.network.model.Video

class VideoListAdapter : RecyclerView.Adapter<VideoListViewHolder>() {

    var videoList: List<Video> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val dataBinding = ViewVideoListItemBinding.inflate(inflater, parent, false)
        return VideoListViewHolder(dataBinding)
    }

    override fun getItemCount() = videoList.size

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        holder.setup(videoList[position])
    }

    fun updateVideoList(videoList: List<Video>) {
        this.videoList = videoList
        notifyDataSetChanged()
    }
}