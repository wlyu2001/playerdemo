package com.shishiapp.playerdemo.view

import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.shishiapp.playerdemo.BR
import com.shishiapp.playerdemo.R
import com.shishiapp.playerdemo.model.Video
import com.shishiapp.playerdemo.network.PlexService
import com.squareup.picasso.Picasso

class VideoListViewHolder constructor(private val dataBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(dataBinding.root) {

    private val thumbImageView = itemView.findViewById<ImageView>(R.id.imageview_thumb)

    fun setup(video: Video) {
        dataBinding.setVariable(BR.itemData, video)
        dataBinding.executePendingBindings()

        Picasso.get().load(PlexService.getMediaUrl(video.thumb)).into(thumbImageView)

        itemView.setOnClickListener {
            val bundle = bundleOf("key" to video.key)
            itemView.findNavController()
                .navigate(R.id.action_videoListFragment_to_videoDetailFragment, bundle)
        }
    }

}