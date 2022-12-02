package com.pandadevs.heyfix_worker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pandadevs.heyfix_worker.data.model.UserGet
import com.pandadevs.heyfix_worker.provider.RequestServiceProvider

class RequestServiceViewModel : ViewModel() {

    var clientData: MutableLiveData<UserGet> = MutableLiveData()

    var isClientAvailable: MutableLiveData<Boolean> = MutableLiveData()

    suspend fun getClientData(id: String) {
        val data: UserGet = RequestServiceProvider.getClientData(id)
        clientData.postValue(data)
    }

    suspend fun isUserAvailable(userId: String) {
        val data: Boolean = RequestServiceProvider.isUserAvailable(userId)
        isClientAvailable.postValue(data)
    }


}