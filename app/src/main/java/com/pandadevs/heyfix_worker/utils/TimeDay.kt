package com.pandadevs.heyfix_worker.utils

object TimeDay {
    fun getTime(): String {
        var result = "Buenas noches,"
        when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
            in 5..11 -> result = "Buenos días,"
            in 12..18 -> result = "Buenas tardes,"
            in 19..24 -> result = "Buenas noches,"
        }
        return result
    }
}