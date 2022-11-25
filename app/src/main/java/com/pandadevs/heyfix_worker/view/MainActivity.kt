package com.pandadevs.heyfix_worker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.GeoPoint
import com.pandadevs.heyfix_worker.R
import com.pandadevs.heyfix_worker.databinding.ActivityMainBinding
import com.pandadevs.heyfix_worker.provider.LocationLiveDataProvider
import com.pandadevs.heyfix_worker.provider.UserLastProvider

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigation: BottomNavigationView = binding.bnvNavigation
        val navController = findNavController(R.id.fragmentNavHost)
        navigation.setupWithNavController(navController)
        initServiceLocationLiveData()
    }

    private fun initServiceLocationLiveData() {
        LocationLiveDataProvider(this).observe(this) {
            val currentPosition = GeoPoint(it.latitude, it.longitude)
            UserLastProvider.setLastCurrentPosition(this@MainActivity, currentPosition)
        }

    }

    override fun onResume() {
        super.onResume()
        UserLastProvider.setLastOnline(this)
        UserLastProvider.getAndSetToken(this)
    }


}