package com.pandadevs.heyfix_worker.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.pandadevs.heyfix_worker.databinding.ActivityLoginBinding
import com.pandadevs.heyfix_worker.utils.SnackbarShow
import com.pandadevs.heyfix_worker.utils.Validations.fieldNotEmpty
import com.pandadevs.heyfix_worker.utils.Validations.fieldRegexEmail

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var editsInputsList: List<TextInputLayout> = listOf()
    private var areCorrectFieldsList: MutableList<Boolean> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        editsInputsList = listOf(binding.etEmail, binding.etPassword)
        areCorrectFieldsList = mutableListOf(false, false)
        binding.btnRegister.setOnClickListener { goToActivity(RegisterActivity::class.java) }
        binding.btnLogin.setOnClickListener { checkFields() }
        activeEventListenerOnEditText()
    }

    private fun checkFields() {
        if (areCorrectFieldsList.none { !it }) {
            SnackbarShow.showSnackbar(binding.root, "Inicio exitoso")
            goToActivity(MainActivity::class.java)
        } else {
            editsInputsList.forEachIndexed { index, it ->
                if (!areCorrectFieldsList[index])
                    it.error = "* Requerido"
            }
        }
    }

    private fun goToActivity(cls: Class<*>) {
        startActivity(Intent(this@LoginActivity, cls))
    }

    private fun activeEventListenerOnEditText() {
        binding.etEmail.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[0] = fieldNotEmpty(editsInputsList[0], text.toString()) && fieldRegexEmail(editsInputsList[0], text.toString())
        }

        binding.etPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[1] = fieldNotEmpty(editsInputsList[1], text.toString())
        }
    }


}