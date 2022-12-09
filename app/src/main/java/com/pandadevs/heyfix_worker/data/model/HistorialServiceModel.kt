package com.pandadevs.heyfix_worker.data.model

import com.google.firebase.Timestamp

data class HistorialServiceModel(
    var client_name: String,
    var address: String,
    var canceled: Boolean,
    var completed: Boolean,
    var date_hired: Timestamp,
    var ranked: Int
)