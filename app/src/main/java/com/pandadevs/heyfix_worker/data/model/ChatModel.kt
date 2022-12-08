package com.pandadevs.heyfix_worker.data.model

import java.io.Serializable

data class ChatModel(
    var id: String,
    var client_name: String,
    var client_picture: String
) : Serializable
