package com.marsgeek.translator

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.marsgeek.translator.ui.HistoryFragment
import com.marsgeek.translator.ui.HomeFragment
import com.marsgeek.translator.ui.TranslateFragment

/**
 * Phase 2: hosts Home / Translate / History fragments via bottom navigation.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        nav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_translate -> TranslateFragment()
                R.id.nav_history -> HistoryFragment()
                else -> HomeFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            true
        }
        if (savedInstanceState == null) {
            nav.selectedItemId = R.id.nav_home
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navView = findViewById<BottomNavigationView>(R.id.bottom_nav)
                if (navView.selectedItemId != R.id.nav_home) {
                    navView.selectedItemId = R.id.nav_home
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }
}
