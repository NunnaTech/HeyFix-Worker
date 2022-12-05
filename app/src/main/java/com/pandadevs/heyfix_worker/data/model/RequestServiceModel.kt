package com.pandadevs.heyfix_worker.data.model

import com.google.firebase.firestore.GeoPoint

data class RequestServiceModel(
    var id:String,
    var accepted: String,
    var address: String,
    var client_id: String,
    var client_ubication: GeoPoint,
    var worker_id: String,
)