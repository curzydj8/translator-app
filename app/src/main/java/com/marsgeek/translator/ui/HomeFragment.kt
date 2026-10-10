package com.marsgeek.translator.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.marsgeek.translator.R

/** Phase 2: home page with entry buttons to Translate and History. */
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
        view.findViewById<Button>(R.id.btn_go_translate).setOnClickListener {
            nav.selectedItemId = R.id.nav_translate
        }
        view.findViewById<Button>(R.id.btn_go_history).setOnClickListener {
            nav.selectedItemId = R.id.nav_history
        }
    }
}
