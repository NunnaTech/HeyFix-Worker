package com.pandadevs.heyfix_worker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pandadevs.heyfix_worker.provider.LoginProvider

class LoginViewModel: ViewModel() {
    var result: MutableLiveData<Boolean> = MutableLiveData()
    var error: MutableLiveData<Boolean> = MutableLiveData()

    suspend fun loginEmail(email:String,password:String){
        var response = LoginProvider.loginWithEmail(email, password)
        when(response){
            "success"->{
                result.postValue(true)
            }
            "error"->{
                error.postValue(false)
            }
            else ->{
                error.postValue(false)
            }
        }
    }


}