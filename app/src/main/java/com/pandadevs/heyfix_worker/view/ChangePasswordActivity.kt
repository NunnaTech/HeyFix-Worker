package com.pandadevs.heyfix_worker.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.pandadevs.heyfix_worker.databinding.ActivityChangePasswordBinding
import com.pandadevs.heyfix_worker.utils.SnackbarShow
import com.pandadevs.heyfix_worker.utils.Validations
import com.pandadevs.heyfix_worker.utils.Validations.fieldNotEmpty

class ChangePasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityChangePasswordBinding
    private var editsInputsList: List<TextInputLayout> = listOf()
    private var areCorrectFieldsList: MutableList<Boolean> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tbApp.setNavigationOnClickListener { finish() }

        editsInputsList = listOf(binding.etEmail, binding.etNewPassword, binding.etRepeatNewPassword)
        areCorrectFieldsList = mutableListOf(false, false, false)
        activeEventListenerOnEditText()

        binding.btnChangePass.setOnClickListener { checkFields() }

    }

    private fun checkFields() {
        var user = FirebaseAuth.getInstance().currentUser
        var newPassword = binding.etNewPassword.editText.toString()
        if(user!=null){
            if (areCorrectFieldsList.none { !it } && checkPasswordsFields()) {
                user!!.updatePassword(newPassword).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        SnackbarShow.showSnackbar(binding.root, "Cambio exitoso")
                    } else {
                        SnackbarShow.showSnackbar(binding.root, "Error al actualizar")
                    }
                }
            } else {
                editsInputsList.forEachIndexed { index, it ->
                    if (!areCorrectFieldsList[index])
                        it.error = "* Requerido"
                }
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
        binding.etEmail.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[0] =
                fieldNotEmpty(editsInputsList[0], text.toString()) && Validations.fieldRegexEmail(
                    editsInputsList[0],
                    text.toString()
                )
        }
        binding.etNewPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[1] = fieldNotEmpty(editsInputsList[1], text.toString())
        }
        binding.etRepeatNewPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[2] = fieldNotEmpty(editsInputsList[2], text.toString())
        }
    }
}