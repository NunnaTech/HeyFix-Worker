package com.pandadevs.heyfix_worker.provider

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.pandadevs.heyfix_worker.data.model.HiredServiceModel
import com.pandadevs.heyfix_worker.data.model.UserGet
import kotlinx.coroutines.CompletableDeferred

class HiredServiceProvider {

    companion object {
        suspend fun getDataHiredService(id: String): HiredServiceModel {
            val response = CompletableDeferred<HiredServiceModel>()
            FirebaseFirestore
                .getInstance()
                .collection("hired_service")
                .document(id)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val data = it.data
                        val hiredService = HiredServiceModel(
                            id = id,
                            client_ubication = data?.get("client_ubication") as GeoPoint,
                            client_name = data["client_name"].toString(),
                            worker_ubication = data["worker_ubication"] as GeoPoint,
                            worker_name = data["worker_name"].toString(),
                            date_hired = data["date_hired"] as Timestamp,
                            address = data["address"].toString(),
                            ranked = data["ranked"].toString().toInt(),
                            completed = data["ranked"].toString().toBoolean(),
                            canceled = data["canceled"].toString().toBoolean(),
                            arrived = data["arrived"].toString().toBoolean(),
                            review = data["review"].toString(),
                            client_id = data["client_id"].toString(),
                            worker_id = data["worker_id"].toString(),
                            category_id = data["category_id"].toString(),
                            category_name = ""
                        )
                        response.complete(hiredService)
                    }
                }

            return response.await()
        }

        suspend fun getDataClient(id: String): UserGet {
            val response = CompletableDeferred<UserGet>()
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(id)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val data = it.data
                        val user = UserGet(
                            id = data?.get("id").toString(),
                            name = data?.get("name").toString(),
                            first_surname = data?.get("first_surname").toString(),
                            second_surname = data?.get("second_surname").toString(),
                            active = data?.get("active").toString().toBoolean(),
                            client = data?.get("client").toString().toBoolean(),
                            email = data?.get("email").toString(),
                            phone_number = data?.get("phone_number").toString(),
                            picture = data?.get("picture").toString(),
                            ranked_avg = data?.get("ranked_avg").toString().toDouble(),
                            transport = data?.get("transport").toString(),
                            category_id = data?.get("category_id").toString(),
                            current_position = data?.get("current_position").toString(),
                        )
                        response.complete(user)
                    }
                }

            return response.await()
        }

        fun statusService(id: String, status: String) {
            FirebaseFirestore
                .getInstance()
                .collection("hired_service")
                .document(id)
                .update(status, true)
        }

        fun activeUsers(idClient: String, idWorker: String){
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(idClient)
                .update("active", true)
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(idWorker)
                .update("active", true)
        }
    }
}