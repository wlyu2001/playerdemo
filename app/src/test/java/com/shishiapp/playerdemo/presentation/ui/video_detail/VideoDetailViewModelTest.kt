package com.shishiapp.playerdemo.presentation.ui.video_detail

import com.shishiapp.playerdemo.repository.FakeRepositoryImp
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class VideoDetailViewModelTest {
    private lateinit var viewModel: VideoDetailViewModel

    @Before
    fun setup() {
        viewModel = VideoDetailViewModel(FakeRepositoryImp())
    }

    @Test
    fun `fetch video with existing key returns video`() {

    }

    @Test
    fun `fetch video with non-existing key return null`() {

    }
}