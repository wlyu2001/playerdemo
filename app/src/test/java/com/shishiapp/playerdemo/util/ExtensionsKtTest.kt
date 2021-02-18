package com.shishiapp.playerdemo.util

import com.google.common.truth.Truth.assertThat
import com.shishiapp.playerdemo.toDurationString
import org.junit.Test

class ExtensionsKtTest {
    @Test
    fun `convert 0 to duration string`() {
        val durationString = 0L.toDurationString()
        assertThat(durationString).isEqualTo("0:00")
    }

    @Test
    fun `convert 60,000 to duration string`() {
        val durationString = 60000L.toDurationString()
        assertThat(durationString).isEqualTo("1:00")
    }

    @Test
    fun `convert 3,600,000 to duration string`() {
        val durationString = 3600000L.toDurationString()
        assertThat(durationString).isEqualTo("1:00:00")
    }

    @Test
    fun `convert 36,000,000 to duration string`() {
        val durationString = 36000000L.toDurationString()
        assertThat(durationString).isEqualTo("10:00:00")
    }

    @Test
    fun `convert 360,000,000 to duration string`() {
        val durationString = 360000000L.toDurationString()
        assertThat(durationString).isEqualTo("100:00:00")
    }
}