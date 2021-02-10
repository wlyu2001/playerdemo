package com.shishiapp.playerdemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.shishiapp.playerdemo.R
import com.shishiapp.playerdemo.network.PlexService

class TokenFragment : Fragment() {

    val token = "XasryqPNGxgcHva-xAyf"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_token, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.textview_token).text = token

        view.findViewById<Button>(R.id.button_token).setOnClickListener {
            PlexService.checkToken(token) { success ->
                if (success) {
                    PlexService.initService(token)
                    findNavController().navigate(R.id.action_tokenFragment_to_sectionFragment)
                } else {
                    Snackbar.make(view, "Can not connect", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}