package com.shishiapp.playerdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shishiapp.playerdemo.model.Content


fun Context.PlayerIntent(content: Content): Intent {
    return Intent(this, PlayerActivity::class.java).apply {
        val partKeys = content.media?.let { it.parts.map{ part -> part.key} }
            ?.toTypedArray()
            ?: emptyArray()


        putExtra(INTENT_PART_KEYS, partKeys)
    }
}

private const val INTENT_PART_KEYS = "part_keys"

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val partKeys = intent.getStringArrayExtra(INTENT_PART_KEYS)
val i = 0

    }


}