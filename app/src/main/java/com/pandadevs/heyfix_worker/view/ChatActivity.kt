package com.pandadevs.heyfix_worker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.pandadevs.heyfix_worker.adapters.SmsAdapter
import com.pandadevs.heyfix_worker.data.model.ChatModel
import com.pandadevs.heyfix_worker.data.model.SmsModel
import com.pandadevs.heyfix_worker.databinding.ActivityChatBinding
import com.pandadevs.heyfix_worker.utils.SnackbarShow

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    lateinit var chatModel: ChatModel
    lateinit var adapter: SmsAdapter
    var model = mutableListOf<SmsModel>()

    companion object{
        const val CHAT_MODEL = "CHAT_MODEL"
        const val SMS_USER = "WORKER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initObservers()
    }

    private fun initView() {
        chatModel = intent.getSerializableExtra(CHAT_MODEL) as ChatModel
        Glide.with(this).load(chatModel.client_picture).into(binding.civPicture)
        binding.tvName.text = chatModel.client_name
        binding.tbApp.setNavigationOnClickListener { finish() }
        binding.btnSend.setOnClickListener { checkMessage() }
    }

    private fun checkMessage() {
        val message = binding.etMessage.editText?.text.toString()
        if (message.isNotEmpty()) {
            sendMessageFirebase(SmsModel(SMS_USER, message))
            binding.etMessage.error = null
            binding.etMessage.editText?.text?.clear()
        } else {
            binding.etMessage.error = "Debes escribir un mensaje"
        }
    }

    private fun setData() {
        adapter = SmsAdapter(this)
        binding.lvChat.adapter = adapter
        adapter.notifyDataSetChanged()
        loadMessagesList()
    }

    private fun sendMessage(data: SmsModel) {
        adapter.add(data)
        model.add(data)
        adapter.notifyDataSetChanged()
        binding.lvChat.setSelection(adapter.count - 1)
    }

    private fun sendMessageFirebase(data: SmsModel) {
        sendMessage(data)
        FirebaseDatabase
            .getInstance()
            .getReference(chatModel.id)
            .setValue(model)
    }

    private fun loadMessagesList() {
        FirebaseDatabase
            .getInstance()
            .getReference(chatModel.id)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    for (item in it.children) {
                        sendMessage(
                            SmsModel(
                                item.child("user").value.toString(),
                                item.child("message").value.toString()
                            )
                        )
                    }
                }
            }
    }

    private fun initObservers() {
        FirebaseDatabase
            .getInstance()
            .getReference(chatModel.id)
            .limitToLast(1)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val smsModel = SmsModel(
                        snapshot.child("user").value.toString(),
                        snapshot.child("message").value.toString()
                    )
                    if (smsModel.user != SMS_USER) sendMessage(smsModel)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(snapshot: DataSnapshot) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {}
            })

        setData()
    }

}