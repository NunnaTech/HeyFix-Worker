package com.pandadevs.heyfix_worker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pandadevs.heyfix_worker.R
import com.pandadevs.heyfix_worker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigation: BottomNavigationView = binding.bnvNavigation
        val navController = findNavController(R.id.fragmentNavHost)
        navigation.setupWithNavController(navController)

    }

}