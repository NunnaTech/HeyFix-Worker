package com.pandadevs.heyfix_worker.provider

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CompletableDeferred

class LoginProvider {

    companion object{

        suspend fun loginWithEmail(email:String,password:String):String?{
            val response = CompletableDeferred<String?>()
            FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        response.complete("success")
                    }else{
                        response.complete("error")
                    }
                }
            return response.await()
        }

    }

}