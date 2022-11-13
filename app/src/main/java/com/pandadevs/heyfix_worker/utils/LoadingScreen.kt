package com.pandadevs.heyfix_worker.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import com.pandadevs.heyfix_worker.R

object LoadingScreen {
    var dialog:Dialog? = null

    fun show(context: Context,message:String,cancelable:Boolean){
        dialog = Dialog(context)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog)
        dialog!!
            .window!!
            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(cancelable)
        var label = dialog!!.findViewById<TextView>(R.id.label)
        label.setText(message)
        dialog!!.show()
    }

    fun hide(){
        if(dialog != null){
            dialog!!.dismiss()
        }
    }

}