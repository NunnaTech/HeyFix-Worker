package com.pandadevs.heyfix_worker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pandadevs.heyfix_worker.data.model.UserPost
import com.pandadevs.heyfix_worker.provider.RegisterProvider

class RegisterViewModel:ViewModel() {

    var result:MutableLiveData<String> = MutableLiveData()
    var error: MutableLiveData<String> = MutableLiveData()

    suspend fun registerData(user: UserPost, password:String){
        var response = RegisterProvider.registerDataUser(user,password)
        when(response){
            "success"->{
                result.postValue("Registro Exitoso")
            }
            "error"->{
                error.postValue("Error al Registrar")
            }
            else->{
                error.postValue("Error al Registrar")
            }
        }
    }

}