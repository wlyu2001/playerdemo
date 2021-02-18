package com.shishiapp.playerdemo.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.shishiapp.playerdemo.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        NavigationUI.setupActionBarWithNavController(
            this,
            findNavController(this, R.id.nav_host_fragment)
        )
    }


    override fun onSupportNavigateUp() =
        findNavController(this, R.id.nav_host_fragment).navigateUp()
}