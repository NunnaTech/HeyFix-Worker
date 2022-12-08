package com.pandadevs.heyfix_worker.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.GeoPoint
import com.pandadevs.heyfix_worker.R
import com.pandadevs.heyfix_worker.databinding.ActivityMainBinding
import com.pandadevs.heyfix_worker.provider.LocationLiveDataProvider
import com.pandadevs.heyfix_worker.provider.UserLastProvider
import com.pandadevs.heyfix_worker.utils.SharedPreferenceManager
import com.pandadevs.heyfix_worker.viewmodel.HiredServiceViewModel
import com.pandadevs.heyfix_worker.viewmodel.RequestServiceViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        var LATITUD_USER: Double = 0.0
        var LONGITUDE_USER: Double = 0.0
    }

    private val requestServiceViewModel: RequestServiceViewModel by viewModels()
    private val hiredServiceViewModel: HiredServiceViewModel by viewModels()
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val user = SharedPreferenceManager(this).getUser()!!
        bottomNavigationView = findViewById(R.id.bnvNavigation)
        navController = findNavController(R.id.fragmentNavHost)
        bottomNavigationView.setupWithNavController(navController)
        lifecycleScope.launch {
            requestServiceViewModel.isThereACurrentRequest(user.id)
            hiredServiceViewModel.isThereACurrentServiceBoolean(user.id)
        }
    }

    private fun initObservers() {
        requestServiceViewModel.isThereACurrentRequest.observe(this) {
            if (it != null && it.accepted.isNullOrEmpty()) {
                goToServiceFragment()
            }
        }

        hiredServiceViewModel.isThereACurrentServiceBoolean.observe(this) {
            if (it != null) {
                val intent = Intent(this, RequestServiceActivity::class.java)
                intent.putExtra(RequestServiceActivity.ID_SERVICE_HIRED, it.id)
                startActivity(intent)
            }
        }
    }

    private fun initServiceLocationLiveData() {
        LocationLiveDataProvider(this).observe(this) {
            LATITUD_USER = it.latitude
            LONGITUDE_USER = it.longitude
            UserLastProvider.setLastCurrentPosition(this, GeoPoint(it.latitude, it.longitude))
        }
        UserLastProvider.getAndSetToken(this)
        UserLastProvider.setLastOnline(this)
    }


    private fun goToServiceFragment() {
        NavigationUI.onNavDestinationSelected(
            bottomNavigationView.menu.getItem(1),
            navController
        )
    }

    override fun onResume() {
        super.onResume()
        initObservers()
        initServiceLocationLiveData()
    }
}