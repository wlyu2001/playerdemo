package com.shishiapp.playerdemo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.shishiapp.playerdemo.R
import com.shishiapp.playerdemo.network.PlexService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment : Fragment() {

    @Inject
    lateinit var plexService: PlexService
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_start).setOnClickListener {
            plexService.connect() { success ->
                if (success) {
                    plexService.initService()
                    findNavController().navigate(R.id.action_startFragment_to_sectionFragment)
                } else {
                    Snackbar.make(view, "Can not connect", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}