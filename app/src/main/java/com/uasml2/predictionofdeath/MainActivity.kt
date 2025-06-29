package com.uasml2.predictionofdeath

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.uasml2.predictionofdeath.fragment.*

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        val toggle = androidx.appcompat.app.ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_about -> showFragment(AboutFragment())
                R.id.nav_features -> showFragment(FeaturesFragment())
                R.id.nav_model -> showFragment(ModelArchitectureFragment())
                R.id.nav_simulate -> showFragment(SimulationFragment())
                R.id.nav_dataset -> showFragment(DatasetFragment())
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Default fragment
        if (savedInstanceState == null) {
            showFragment(AboutFragment())
            navView.setCheckedItem(R.id.nav_about)
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
