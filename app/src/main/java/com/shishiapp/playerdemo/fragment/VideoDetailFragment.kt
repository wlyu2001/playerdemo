package com.shishiapp.playerdemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shishiapp.playerdemo.R
import com.shishiapp.playerdemo.databinding.FragmentVideoDetailBinding
import com.shishiapp.playerdemo.network.PlexService
import com.shishiapp.playerdemo.playerIntent
import com.shishiapp.playerdemo.toDurationString
import com.shishiapp.playerdemo.viewmodel.VideoDetailViewModel
import com.squareup.picasso.Picasso
import java.util.*


class VideoDetailFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentVideoDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewDataBinding = FragmentVideoDetailBinding.inflate(inflater, container, false).apply {

            viewmodel =
                ViewModelProvider(this@VideoDetailFragment).get(VideoDetailViewModel::class.java)
            lifecycleOwner = viewLifecycleOwner
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = viewDataBinding.viewmodel
        val imageViewArt = view.findViewById<ImageView>(R.id.imageview_art)
        val textViewTitle = view.findViewById<TextView>(R.id.textview_title)
        val textViewDuration = view.findViewById<TextView>(R.id.textview_duration)
        if (viewModel != null) {
            arguments?.let { it.getString("key")?.let { it1 -> viewModel.fetchContentDetail(it1) } }

            viewModel.contentDetailLive.observe(viewLifecycleOwner, { video ->
                Picasso.get().load(PlexService.getMediaUrl(video.art)).placeholder(R.drawable.plex_logo).into(imageViewArt)
                textViewTitle.text = video.title
                textViewDuration.text = getString(
                    R.string.duration_string, video.duration.toDurationString()
                )

                view.findViewById<Button>(R.id.button_play).setOnClickListener {
                    startActivity(activity?.playerIntent(video))
                }
            })
        }


    }
}