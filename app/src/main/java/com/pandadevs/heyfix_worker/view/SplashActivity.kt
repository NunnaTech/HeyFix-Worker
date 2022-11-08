package com.pandadevs.heyfix_worker.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.pandadevs.heyfix_worker.databinding.ActivitySplashBinding
import com.pandadevs.heyfix_worker.utils.SharedPreferenceManager

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler().postDelayed({
            val active = SharedPreferenceManager(this).getSession()
            if (active!!) startActivity(Intent(this,MainActivity::class.java))
            else startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }, 2000)
    }
}