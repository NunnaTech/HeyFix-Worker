package com.pandadevs.heyfix_worker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.pandadevs.heyfix_worker.R
import com.pandadevs.heyfix_worker.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var editsInputsList: List<TextInputLayout> = listOf()
    private var areCorrectFieldsList: MutableList<Boolean> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        editsInputsList = listOf(
            binding.etName, binding.etLastName, binding.etWork, binding.etTransport,
            binding.etEmail, binding.etPhone, binding.etNewPassword, binding.etRepeatNewPassword
        )
        areCorrectFieldsList = mutableListOf(false, false, false, false, false, false, false, false)

        binding.tbApp.setNavigationOnClickListener { finish() }
        binding.btnRegister.setOnClickListener { checkFields() }
        activeEventListenerOnEditText()

    }

    private fun checkFields() {
        if (areCorrectFieldsList.none { !it } && checkPasswordsFields()) {
            SnackbarShow.showSnackbar(binding.root, "Registro exitoso")
        } else {
            editsInputsList.forEachIndexed { index, it ->
                if (!areCorrectFieldsList[index])
                    it.error = "* Requerido"
            }
        }
    }

    private fun checkPasswordsFields(): Boolean {
        val areCorrectPasswords =
            editsInputsList[5].editText?.text.toString() == editsInputsList[6].editText?.text.toString()
        if (!areCorrectPasswords) {
            editsInputsList[5].error = "* Requerido"
            editsInputsList[6].error = "* Requerido"
            SnackbarShow.showSnackbar(binding.root, "Las contraseñas no coinciden")
        }
        return areCorrectPasswords
    }

    private fun activeEventListenerOnEditText() {
        binding.etName.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[0] =
                fieldNotEmpty(editsInputsList[0], text.toString(), 2) && fieldRegexName(
                    editsInputsList[0],
                    text.toString()
                )
        }

        binding.etLastName.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[1] =
                fieldNotEmpty(editsInputsList[1], text.toString(), 2) && fieldRegexName(
                    editsInputsList[1],
                    text.toString()
                )
        }

        binding.etWork.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[2] = fieldNotEmpty(editsInputsList[2], text.toString(), 4)
        }

        binding.etTransport.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[3] = fieldNotEmpty(editsInputsList[3], text.toString(), 4)
        }

        binding.etEmail.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[4] =
                fieldNotEmpty(editsInputsList[4], text.toString()) && fieldRegexEmail(
                    editsInputsList[4],
                    text.toString()
                )
        }

        binding.etPhone.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[5] = fieldNotEmpty(editsInputsList[5], text.toString(), 10)
        }

        binding.etNewPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[6] = fieldNotEmpty(editsInputsList[6], text.toString())
        }

        binding.etRepeatNewPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[7] = fieldNotEmpty(editsInputsList[7], text.toString())
        }
    }

    private fun fieldRegexName(field: TextInputLayout, text: String): Boolean {
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

    private fun fieldRegexEmail(field: TextInputLayout, text: String): Boolean {
        val isCorrectField = Regex("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}").matches(text)
        if (isCorrectField) {
            field.error = null
            field.helperText = "* Requerido"
        } else {
            field.helperText = null
            field.error = "Debe ser un correo válido"
        }
        return isCorrectField
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