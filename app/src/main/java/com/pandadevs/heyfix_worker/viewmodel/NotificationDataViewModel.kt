package com.pandadevs.heyfix_worker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pandadevs.heyfix_worker.data.model.NotificationDataModel

class NotificationDataViewModel : ViewModel() {

    private val notification = MutableLiveData<NotificationDataModel?>()
    val notificationSelected: MutableLiveData<NotificationDataModel?> get() = notification


    fun selectedNotification(notificationDataModel: NotificationDataModel) {
        notification.value = notificationDataModel
    }

    fun clearNotification() {
        notification.value = null
    }
}