package com.pandadevs.heyfix_worker.utils

import com.google.android.material.textfield.TextInputLayout

object Validations {

    fun fieldNotEmpty(field: TextInputLayout, text: String, min: Int = 6): Boolean {
        val isCorrectField = text.isNotEmpty() && text.length >= min
        if (isCorrectField) {
            field.error = null
            field.helperText = "* Requerido"
        } else {
            field.helperText = null
            field.error = "Debe contener al menos $min caracteres"
        }
        return isCorrectField
    }

    fun fieldRegexName(field: TextInputLayout, text: String): Boolean {
        val isCorrectField = Regex("^[A-Za-zÁÉÍÓÚÑáéíóúñ. ]*$").matches(text)
        if (isCorrectField) {
            field.error = null
            field.helperText = "* Requerido"
        } else {
            field.helperText = null
            field.error = "Debe de contener caracteres válidos"
        }
        return isCorrectField
    }

    fun fieldRegexEmail(field: TextInputLayout, text: String): Boolean {
        val isCorrectField = Regex("[A-Za-z0-9]+@[A-Za-z]+\\.[A-Za-z]{2,3}").matches(text)
        if (isCorrectField) {
            field.error = null
            field.helperText = "* Requerido"
        } else {
            field.helperText = null
            field.error = "Debe ser un correo válido"
        }
        return isCorrectField
    }
}