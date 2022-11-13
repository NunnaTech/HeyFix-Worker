package com.pandadevs.heyfix_worker.data.model

data class UserPost (
    var name:String,
    var first_surname:String,
    var second_surname:String,
    var active:Boolean,
    var client:Boolean,
    var email:String,
    var phone_number:String,
    var picture:String,
    var ranked_avg:Double,
    var transport:String,
    var category_id:String,
    var current_position:String,
)