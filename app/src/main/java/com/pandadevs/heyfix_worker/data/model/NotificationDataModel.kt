package com.pandadevs.heyfix_worker.data.model

import java.io.Serializable

data class NotificationDataModel(
    var id: String,
    var title: String,
    var address: String,
    var client_id: String,
    var worker_id: String,
): Serializable