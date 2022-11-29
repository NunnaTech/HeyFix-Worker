package com.pandadevs.heyfix_worker.provider

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pandadevs.heyfix_worker.R
import com.pandadevs.heyfix_worker.view.MainActivity

class MessagingServiceProvider : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.data.isNotEmpty()) {
            createNotificationSystem(message.data)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }


    private fun createNotificationSystem(message: MutableMap<String, String>) {
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.putExtra(MainActivity.NOTIFICATION_ID, message["id"])
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_ONE_SHOT)
        val channel = getString(R.string.app_name)
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification = NotificationCompat
            .Builder(this, channel)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(message["title"])
            .setContentText("Pulsa para ver los detalles")
            .setAutoCancel(true)
            .setSound(sound)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel2 = NotificationChannel(
                channel,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel2)
        }
        notificationManager.notify(0, notification.build())
    }
}