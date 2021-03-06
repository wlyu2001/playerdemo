package com.shishiapp.playerdemo.presentation.ui.video_list

import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.shishiapp.playerdemo.BR
import com.shishiapp.playerdemo.R
import com.shishiapp.playerdemo.getMediaUrl
import com.shishiapp.playerdemo.network.model.Video
import com.squareup.picasso.Picasso


class VideoListViewHolder constructor(private val dataBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(dataBinding.root) {

    private val thumbImageView = itemView.findViewById<ImageView>(R.id.imageview_thumb)

    fun setup(video: Video) {
        dataBinding.setVariable(BR.itemData, video)
        dataBinding.executePendingBindings()

        Picasso.get().load(video.thumb.getMediaUrl()).placeholder(R.drawable.plex_logo)
            .into(thumbImageView)

        itemView.setOnClickListener {
            val bundle = bundleOf("key" to video.key)
            itemView.findNavController()
                .navigate(R.id.action_videoListFragment_to_videoDetailFragment, bundle)
        }
    }

}