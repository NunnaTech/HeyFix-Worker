package com.pandadevs.heyfix_worker.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pandadevs.heyfix_worker.R
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
        initView()
        activeEventListenerOnEditText()
    }

    private fun initView() {
        editsInputsList =
            listOf(binding.etOldPassword, binding.etNewPassword, binding.etRepeatNewPassword)
        areCorrectFieldsList = mutableListOf(false, false, false)
        val provider = SharedPreferenceManager(this).getProviderEmail()
        if (provider.equals("google")) {
            binding.tvDescription.text =
                "No puedes cambiar tu contraseña si has iniciado sesión con una cuenta de Google"
            binding.etOldPassword.visibility = View.GONE
            binding.etNewPassword.visibility = View.GONE
            binding.etRepeatNewPassword.visibility = View.GONE
            binding.btnChangePass.visibility = View.GONE
            binding.ivGoogle.visibility = View.VISIBLE
        }
        binding.btnChangePass.setOnClickListener { checkFields() }
    }

    private fun checkFields() {
        if (areCorrectFieldsList.none { !it } && checkPasswordsFields()) {
            changePassword()
        } else {
            editsInputsList.forEachIndexed { index, it ->
                if (!areCorrectFieldsList[index])
                    it.error = "* Requerido"
            }
        }
    }

    private fun changePassword() {
        try {
            val userShared = SharedPreferenceManager(this).getUser()!!
            val oldPassword = binding.etOldPassword.editText?.text.toString()
            val newPassword = binding.etNewPassword.editText?.text.toString()
            val credential: AuthCredential =
                EmailAuthProvider.getCredential(userShared.email, oldPassword)
            val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            user?.reauthenticate(credential)?.addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> {
                        user.updatePassword(newPassword).addOnCompleteListener {
                            if (it.isSuccessful) dialogConfirmLogout()
                            else SnackbarShow.showSnackbar(
                                binding.root,
                                "Hubo un error al cambiar la contraseña"
                            )
                        }
                    }
                    else -> SnackbarShow.showSnackbar(
                        binding.root, "La contraseña actual no es correcta"
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }


    private fun dialogConfirmLogout() {
        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        )
            .setTitle("Contraseña actualizada")
            .setIcon(R.drawable.ilus_password)
            .setCancelable(false)
            .setMessage("Tu contraseña ha sido cambiada, regresarás al inicio de sesión")
            .setPositiveButton("Aceptar") { _, _ -> logout() }
            .show()
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        SharedPreferenceManager(this).cleanShared()
        val intent = Intent(this, SplashActivity::class.java)
        finishAffinity()
        startActivity(intent)
    }

    private fun checkPasswordsFields(): Boolean {
        val areCorrectPasswords = editsInputsList[0].editText?.text.toString()
            .isNotEmpty() && editsInputsList[1].editText?.text.toString() == editsInputsList[2].editText?.text.toString()
        if (!areCorrectPasswords) {
            editsInputsList[1].error = "* Requerido"
            editsInputsList[2].error = "* Requerido"
            SnackbarShow.showSnackbar(binding.root, "Las contraseñas no coinciden o estan vacías")
        }
        return areCorrectPasswords
    }

    private fun activeEventListenerOnEditText() {
        binding.etOldPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[0] = fieldNotEmpty(editsInputsList[0], text.toString())
        }
        binding.etNewPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[1] = fieldNotEmpty(editsInputsList[1], text.toString())
        }
        binding.etRepeatNewPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[2] = fieldNotEmpty(editsInputsList[2], text.toString())
        }

    }
}