package com.pandadevs.heyfix_worker.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pandadevs.heyfix_worker.databinding.ActivityRequestServiceBinding

class RequestServiceActivity : AppCompatActivity() {
    lateinit var binding: ActivityRequestServiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChat.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        binding.tbApp.setNavigationOnClickListener { finish() }
    }
}