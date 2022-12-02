package com.pandadevs.heyfix_worker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.GeoPoint
import com.pandadevs.heyfix_worker.R
import com.pandadevs.heyfix_worker.data.model.NotificationDataModel
import com.pandadevs.heyfix_worker.databinding.ActivityMainBinding
import com.pandadevs.heyfix_worker.provider.LocationLiveDataProvider
import com.pandadevs.heyfix_worker.provider.UserLastProvider
import com.pandadevs.heyfix_worker.viewmodel.NotificationDataViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        const val NOTIFICATION_DATA = "NOTIFICATION_DATA"
    }

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var navigation: BottomNavigationView

    private val notificationDataViewModel: NotificationDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigation = binding.bnvNavigation
        navController = findNavController(R.id.fragmentNavHost)
        navigation.setupWithNavController(navController)
        initServiceLocationLiveData()
    }

    private fun initServiceLocationLiveData() {
        LocationLiveDataProvider(this).observe(this) {
            UserLastProvider.setLastCurrentPosition(
                this@MainActivity,
                GeoPoint(it.latitude, it.longitude)
            )
        }
        UserLastProvider.getAndSetToken(this)
    }

    override fun onStart() {
        super.onStart()

        UserLastProvider.setLastOnline(this)
        if (intent.hasExtra(NOTIFICATION_DATA)) {
            val notificationData: NotificationDataModel =
                intent.getSerializableExtra(NOTIFICATION_DATA) as NotificationDataModel
            notificationDataViewModel.selectedNotification(notificationData)
            navController.navigate(R.id.action_itHome_to_itServices)
            intent.removeExtra(NOTIFICATION_DATA)
        }
    }


}