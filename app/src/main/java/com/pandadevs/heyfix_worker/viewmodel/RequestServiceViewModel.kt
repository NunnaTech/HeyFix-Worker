package com.pandadevs.heyfix_worker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.GeoPoint
import com.pandadevs.heyfix_worker.data.model.NotificationDataModel
import com.pandadevs.heyfix_worker.data.model.UserGet
import com.pandadevs.heyfix_worker.provider.RequestServiceProvider
import com.pandadevs.heyfix_worker.utils.SharedPreferenceManager

class RequestServiceViewModel : ViewModel() {

    var clientData: MutableLiveData<UserGet> = MutableLiveData()
    var isClientAvailable: MutableLiveData<Boolean> = MutableLiveData()
    var takeServiceCompleted: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    suspend fun getClientData(id: String) {
        val data: UserGet = RequestServiceProvider.getClientData(id)
        clientData.postValue(data)
    }

    suspend fun isUserAvailable(userId: String) {
        val data: Boolean = RequestServiceProvider.isUserAvailable(userId)
        isClientAvailable.postValue(data)
    }

    suspend fun takeService(
        notification: NotificationDataModel,
        userUbication: GeoPoint,
        userCategoryId: String
    ) {
        val data: Boolean =
            RequestServiceProvider.takeService(notification, userUbication, userCategoryId)
        takeServiceCompleted.value = data
    }


}