package com.pandadevs.heyfix_worker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.pandadevs.heyfix_worker.R
import com.pandadevs.heyfix_worker.data.model.SmsModel
import com.pandadevs.heyfix_worker.view.ChatActivity

class SmsAdapter(context: Context) : BaseAdapter() {

    var mensajes = ArrayList<SmsModel>()
    var globalContext = context

    fun add(data: SmsModel) {
        mensajes.add(data)
        notifyDataSetChanged()
    }

    override fun getCount(): Int = mensajes.size

    override fun getItem(position: Int): Any = mensajes[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val viewHolder = SmsViewHolder()
        var myView = view
        val messageView = LayoutInflater.from(globalContext)
        val message = mensajes[position].message
        if (mensajes[position].user == ChatActivity.SMS_USER) {
            myView = messageView.inflate(R.layout.my_sms, null)
            viewHolder.message = myView.findViewById(R.id.tvMensaje)
            viewHolder.message!!.text = message
        }else{
            myView = messageView.inflate(R.layout.your_sms, null)
            viewHolder.message = myView.findViewById(R.id.tvMensaje)
            viewHolder.message!!.text = message
        }
        return myView!!
    }
}

internal class SmsViewHolder {
    var message: TextView? = null
}