package com.shishiapp.playerdemo.view

import androidx.core.os.bundleOf
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.shishiapp.playerdemo.BR
import com.shishiapp.playerdemo.R
import com.shishiapp.playerdemo.model.Section

class SectionListViewHolder constructor(private val dataBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(dataBinding.root) {

    fun setup(section: Section) {
        dataBinding.setVariable(BR.itemData, section)
        dataBinding.executePendingBindings()


        itemView.setOnClickListener {
            val bundle = bundleOf("key" to section.key)
            itemView.findNavController()
                .navigate(R.id.action_sectionFragment_to_listFragment, bundle)
        }
    }

}