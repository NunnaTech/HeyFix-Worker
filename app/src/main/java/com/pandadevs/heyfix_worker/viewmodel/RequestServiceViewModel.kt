package com.pandadevs.heyfix_worker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.pandadevs.heyfix_worker.data.model.RequestServiceModel
import com.pandadevs.heyfix_worker.data.model.UserGet
import com.pandadevs.heyfix_worker.provider.RequestServiceProvider

class RequestServiceViewModel : ViewModel() {

    var clientData: MutableLiveData<UserGet> = MutableLiveData()
    var isClientAvailable: MutableLiveData<Boolean> = MutableLiveData()
    var takeServiceCompleted: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var isThereACurrentRequest: MutableLiveData<RequestServiceModel?> = MutableLiveData<RequestServiceModel?>()

    suspend fun getClientData(id: String) {
        val data: UserGet = RequestServiceProvider.getClientData(id)
        clientData.value = data
    }

    suspend fun isClientAvailable(userId: String) {
        val data: Boolean = RequestServiceProvider.isUserAvailable(userId)
        isClientAvailable.value = data
    }

    suspend fun takeService(
        requestServiceModel: RequestServiceModel,
        userUbication: GeoPoint,
        userCategoryId: String,
        nameWorker: String,
        nameClient: String,
        address: String
    ) {
        val data: Boolean =
            RequestServiceProvider.takeService(
                requestServiceModel,
                userUbication,
                userCategoryId,
                nameWorker,
                nameClient,
                address
            )
        takeServiceCompleted.value = data
    }

    fun isThereACurrentRequest(idUser: String) {
        FirebaseFirestore
            .getInstance()
            .collection("request_service")
            .whereEqualTo("worker_id", idUser)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshots != null && snapshots.documents.isNotEmpty()) {
                    val data = snapshots.documents[0].data
                    val requestServiceModel = RequestServiceModel(
                        id = snapshots.documents[0].id,
                        address = data?.get("address").toString(),
                        client_id = data?.get("client_id").toString(),
                        worker_id = data?.get("worker_id").toString(),
                        client_ubication = data?.get("client_ubication") as GeoPoint,
                        accepted = data["accepted"].toString()
                    )
                    isThereACurrentRequest.value = requestServiceModel
                } else {
                    isThereACurrentRequest.value = null
                }
            }
    }
}