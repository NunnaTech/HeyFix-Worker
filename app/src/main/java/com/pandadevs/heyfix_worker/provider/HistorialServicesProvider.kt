package com.pandadevs.heyfix_worker.provider

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.pandadevs.heyfix_worker.data.model.HistorialServiceModel
import kotlinx.coroutines.CompletableDeferred

class HistorialServicesProvider {
    companion object{

        suspend fun getHistorialServices(idUser:String):List<HistorialServiceModel>{
            val response = CompletableDeferred<List<HistorialServiceModel>>()

            FirebaseFirestore
                .getInstance()
                .collection("hired_service")
                .whereEqualTo("worker_id", idUser)
                .whereEqualTo("completed", true)
                .whereEqualTo("canceled", false)
                .get()
                .addOnSuccessListener {
                    val list = mutableListOf<HistorialServiceModel>()
                    for (document in it){
                        val service = HistorialServiceModel(
                            client_name = document["client_name"].toString(),
                            address = document["address"].toString(),
                            canceled = document["canceled"].toString().toBoolean(),
                            completed= document["completed"].toString().toBoolean(),
                            date_hired = document["date_hired"] as Timestamp,
                            ranked = document["ranked"].toString().toInt()
                        )
                        list.add(service)
                    }
                    response.complete(list)
                }
                .addOnFailureListener { response.complete(listOf()) }
            return response.await()

        }
    }
}