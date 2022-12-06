package com.pandadevs.heyfix_worker.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.pandadevs.heyfix_worker.databinding.ActivityChangePasswordBinding
import com.pandadevs.heyfix_worker.utils.SharedPreferenceManager
import com.pandadevs.heyfix_worker.utils.SnackbarShow
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

        editsInputsList = listOf(binding.etNewPassword, binding.etRepeatNewPassword)
        areCorrectFieldsList = mutableListOf(false, false)
        activeEventListenerOnEditText()


        var provider = SharedPreferenceManager(this).getProviderEmail()
        if(!!provider.equals("email")){
            binding.etNewPassword.isEnabled = true
            binding.etRepeatNewPassword.isEnabled = true
            binding.txtDescription.setText("No puedes cambiar tu contraseña si inicias sesión con Google")
        }
        binding.btnChangePass.setOnClickListener { checkFields() }
    }

    private fun checkFields() {

        var uidShared =SharedPreferenceManager(this).getUID()
        var userFirebase = FirebaseAuth.getInstance().currentUser
        var uidFirebase = userFirebase!!?.uid
        var newPassword = binding.etNewPassword.editText.toString()


        if(uidShared.equals(uidFirebase)){
            if (areCorrectFieldsList.none { !it } && checkPasswordsFields()) {
                userFirebase.updatePassword(newPassword).addOnCompleteListener{ task->
                    if(task.isSuccessful){
                        FirebaseAuth.getInstance().signOut()
                        startActivity(
                            Intent(this, LoginActivity::class.java)
                        )
                        SnackbarShow.showSnackbar(binding.root, "Cambio exitoso")
                        System.out.println("CAMBIO EXITOSO")
                    }else{
                        SnackbarShow.showSnackbar(binding.root, "Error al actualizar")
                        Toast.makeText(this, "Error " + task.exception, Toast.LENGTH_SHORT)
                            .show()
                        System.out.println("Error: " + task.exception)
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
            editsInputsList[0].editText?.text.toString() == editsInputsList[1].editText?.text.toString()
        if (!areCorrectPasswords) {
            editsInputsList[0].error = "* Requerido"
            editsInputsList[1].error = "* Requerido"
            SnackbarShow.showSnackbar(binding.root, "Las contraseñas no coinciden")
        }
        return areCorrectPasswords
    }

    private fun activeEventListenerOnEditText() {
        binding.etNewPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[0] = fieldNotEmpty(editsInputsList[0], text.toString())
        }
        binding.etRepeatNewPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[1] = fieldNotEmpty(editsInputsList[1], text.toString())
        }
    }
}