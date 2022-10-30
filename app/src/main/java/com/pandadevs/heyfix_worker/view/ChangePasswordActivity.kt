package com.pandadevs.heyfix_worker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.pandadevs.heyfix_worker.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityChangePasswordBinding
    private var editsInputsList: List<TextInputLayout> = listOf()
    private var areCorrectFieldsList: MutableList<Boolean> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tbApp.setNavigationOnClickListener { finish() }

        editsInputsList =
            listOf(binding.etCurrentlyPassword, binding.etNewPassword, binding.etRepeatNewPassword)
        areCorrectFieldsList = mutableListOf(false, false, false)
        activeEventListenerOnEditText()

        binding.btnChangePass.setOnClickListener { checkFields() }

    }

    private fun checkFields() {
        if (areCorrectFieldsList.none { !it } && checkPasswordsFields()) {
            SnackbarShow.showSnackbar(binding.root, "Cambio exitoso")
        } else {
            editsInputsList.forEachIndexed { index, it ->
                if (!areCorrectFieldsList[index])
                    it.error = "* Requerido"
            }
        }
    }

    private fun checkPasswordsFields(): Boolean {
        val areCorrectPasswords =
            editsInputsList[1].editText?.text.toString() == editsInputsList[2].editText?.text.toString()
        if (!areCorrectPasswords) {
            editsInputsList[1].error = "* Requerido"
            editsInputsList[2].error = "* Requerido"
            SnackbarShow.showSnackbar(binding.root, "Las contraseñas no coinciden")
        }
        return areCorrectPasswords
    }

    private fun activeEventListenerOnEditText() {
        binding.etCurrentlyPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[0] = fieldNotEmpty(editsInputsList[0], text.toString())
        }
        binding.etNewPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[1] = fieldNotEmpty(editsInputsList[1], text.toString())
        }
        binding.etRepeatNewPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[2] = fieldNotEmpty(editsInputsList[2], text.toString())
        }
    }

    private fun fieldNotEmpty(field: TextInputLayout, text: String, min: Int = 6): Boolean {
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
}