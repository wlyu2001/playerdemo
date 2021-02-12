package com.shishiapp.playerdemo.view

import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.shishiapp.playerdemo.BR
import com.shishiapp.playerdemo.R
import com.shishiapp.playerdemo.model.Content
import com.shishiapp.playerdemo.network.PlexService
import com.squareup.picasso.Picasso

class ContentListViewHolder constructor(private val dataBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(dataBinding.root) {

    private val thumbImageView = itemView.findViewById<ImageView>(R.id.imageview_thumb)

    fun setup(content: Content) {
        dataBinding.setVariable(BR.itemData, content)
        dataBinding.executePendingBindings()

        Picasso.get().load(PlexService.getImageUrl(content.thumb)).into(thumbImageView)

        itemView.setOnClickListener {
            val bundle = bundleOf("key" to content.key)
            itemView.findNavController()
                .navigate(R.id.action_contentListFragment_to_contentDetailFragment, bundle)
        }
    }

}