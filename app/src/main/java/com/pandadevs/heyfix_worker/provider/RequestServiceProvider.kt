package com.pandadevs.heyfix_worker.provider

import com.google.firebase.firestore.FirebaseFirestore
import com.pandadevs.heyfix_worker.data.model.UserGet
import kotlinx.coroutines.CompletableDeferred

class RequestServiceProvider {
    companion object {
        fun cancelRequestService(id: String) {
            FirebaseFirestore
                .getInstance()
                .collection("request_service")
                .document(id)
                .update(mapOf("accepted" to false))
        }

        suspend fun getClientData(id: String): UserGet {
            val userData = CompletableDeferred<UserGet>()
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(id)
                .get()
                .addOnSuccessListener { u ->
                    userData.complete(
                        UserGet(
                            active = u.data?.get("active").toString().toBoolean(),
                            category_id = u.data?.get("category_id").toString(),
                            client = u.data?.get("client").toString().toBoolean(),
                            current_position = u.data?.get("current_position").toString(),
                            email = u.data?.get("email").toString(),
                            first_surname = u.data?.get("first_surname").toString(),
                            name = u.data?.get("name").toString(),
                            phone_number = u.data?.get("phone_number").toString(),
                            picture = u.data?.get("picture").toString(),
                            ranked_avg = u.data?.get("ranked_avg").toString().toDouble(),
                            second_surname = u.data?.get("second_surname").toString(),
                            transport = u.data?.get("transport").toString(),
                            id = u.reference.id
                        )
                    )
                }
            return userData.await()
        }
    }
}