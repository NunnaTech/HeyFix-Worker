package com.pandadevs.heyfix_worker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pandadevs.heyfix_worker.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tbApp.setNavigationOnClickListener { finish() }
        binding.btnSend.setOnClickListener { checkMessage() }
    }

    private fun checkMessage() {
        if (binding.etMessage.editText?.text.toString().isNotEmpty()) {
            SnackbarShow.showSnackbar(binding.root, "Mensaje enviado")
            binding.etMessage.error = null
            binding.etMessage.editText?.text?.clear()
        } else {
            binding.etMessage.error = "Debes escribir un mensaje"
        }
    }
}