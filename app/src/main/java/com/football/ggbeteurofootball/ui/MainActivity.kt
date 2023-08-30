package com.football.ggbeteurofootball.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.navView.setNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.matchesFragment ||
                item.itemId == R.id.favoritesFragment)
                findNavController(R.id.nav_host_fragment_content_main).navigate(item.itemId)
            else if (item.itemId == R.id.clearCash)
                CasheDialog().show(supportFragmentManager, "DialogCash")
            binding.drawerLayout.closeDrawers()
            true
        }
    }








    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}