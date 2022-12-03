package com.pandadevs.heyfix_worker.provider

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.pandadevs.heyfix_worker.data.model.NotificationDataModel
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


        suspend fun isUserAvailable(userId: String): Boolean {
            val response = CompletableDeferred<Boolean>()
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener {
                    if (it.exists() && it.data?.get("active").toString().toBoolean()) {
                        response.complete(true)
                    }
                }
                .addOnFailureListener {
                    response.complete(false)
                }
            return response.await()
        }


        suspend fun takeService(
            notification: NotificationDataModel,
            userUbication: GeoPoint,
            userCategoryId: String
        ): Boolean {

            val response: CompletableDeferred<Boolean> = CompletableDeferred<Boolean>()
            FirebaseFirestore
                .getInstance()
                .collection("request_service")
                .document(notification.id)
                .update(mapOf("accepted" to true))

            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(notification.client_id)
                .update(mapOf("active" to false))

            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(notification.worker_id)
                .update(mapOf("active" to false))


            FirebaseFirestore
                .getInstance()
                .collection("hired_service")
                .document(notification.id)
                .set(
                    hashMapOf(
                        "worker_ubication" to userUbication,
                        "date_hired" to Timestamp.now(),
                        "ranked" to 0,
                        "completed" to false,
                        "review" to "",
                        "client_id" to notification.client_id,
                        "worker_id" to notification.worker_id,
                        "category_id" to userCategoryId
                    )
                ).addOnSuccessListener {
                    response.complete(true)
                }.addOnFailureListener {
                    response.complete(false)
                }
            return response.await()

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