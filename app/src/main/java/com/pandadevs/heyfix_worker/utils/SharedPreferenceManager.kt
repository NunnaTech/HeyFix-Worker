package com.pandadevs.heyfix_worker.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.pandadevs.heyfix_worker.data.model.UserGet

class SharedPreferenceManager(val context: Context) {
    private val PREFS_NAME = "SHARED_PREF"
    val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)

    fun saveUser(user: UserGet) {
        sharedPref.edit().putString("user", Gson().toJson(user)).apply()
    }

    fun saveSession(){
        sharedPref.edit().putBoolean("active",true).apply()
    }

    fun getSession():Boolean?{
        return sharedPref.getBoolean("active", false)
    }

    fun getUser(): UserGet? {
        val data = sharedPref.getString("user", null) ?: return null
        return Gson().fromJson(data, UserGet::class.java)
    }

    fun cleanShared(){
        sharedPref.edit().clear().apply()
    }
}