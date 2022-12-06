package com.pandadevs.heyfix_worker.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class HiredServiceModel(
    var id:String = "",
    var client_ubication: GeoPoint,
    var client_name: String,
    var worker_ubication: GeoPoint,
    var worker_name: String,
    var date_hired:Timestamp,
    var address: String,
    var ranked: Int,
    var completed: Boolean,
    var canceled: Boolean,
    var arrived: Boolean,
    var review: String,
    var client_id: String,
    var worker_id: String,
    var category_id: String,
    var category_name:String
)