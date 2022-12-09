package com.pandadevs.heyfix_worker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pandadevs.heyfix_worker.data.model.HistorialServiceModel
import com.pandadevs.heyfix_worker.provider.HistorialServicesProvider

class HistorialServicesViewModel : ViewModel() {

    var rateServices = MutableLiveData<Double>()
    var historialServices = MutableLiveData<List<HistorialServiceModel>>()

    suspend fun getHistorialServices(idUser: String) {
        var numberOfCompletes = 0
        var totalRate = 0.0
        val response = HistorialServicesProvider.getHistorialServices(idUser)
        historialServices.value = response
        for (service in response) {
            if (service.completed) {
                numberOfCompletes++
                totalRate += service.ranked
            }
        }
        if (numberOfCompletes > 0 && totalRate > 0.0) rateServices.value =
            totalRate / numberOfCompletes
    }
}