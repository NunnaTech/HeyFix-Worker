package com.pandadevs.heyfix_worker.provider

import android.content.Context
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.messaging.FirebaseMessaging
import com.pandadevs.heyfix_worker.utils.SharedPreferenceManager

class UserLastProvider {

    companion object {
        fun setLastOnline(context: Context) {
            val userId = SharedPreferenceManager(context).getUser()!!.id
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(userId)
                .update(mapOf("last_online" to Timestamp.now()))
        }

        fun setLastCurrentPositionOnHiredService(id:String,location: GeoPoint){
            FirebaseFirestore
                .getInstance()
                .collection("hired_service")
                .document(id)
                .update("worker_ubication", location)
        }

        fun setLastCurrentPosition(context: Context, location: GeoPoint) {
            val userId = SharedPreferenceManager(context).getUser()!!.id
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(userId)
                .update(mapOf("current_position" to location))
        }

        fun getAndSetToken(context: Context) {
            val userId = SharedPreferenceManager(context).getUser()!!.id
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("FIREBASE_SMS", it.result)
                    FirebaseFirestore
                        .getInstance()
                        .collection("users")
                        .document(userId)
                        .update(mapOf("tokenNotification" to it.result))
                }
            }
        }
    }
}