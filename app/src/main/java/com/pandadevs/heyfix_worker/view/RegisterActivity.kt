package com.pandadevs.heyfix_worker.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.pandadevs.heyfix_worker.R
import com.pandadevs.heyfix_worker.data.model.UserPost
import com.pandadevs.heyfix_worker.databinding.ActivityRegisterBinding
import com.pandadevs.heyfix_worker.utils.LoadingScreen
import com.pandadevs.heyfix_worker.utils.SnackbarShow
import com.pandadevs.heyfix_worker.utils.Validations.fieldNotEmpty
import com.pandadevs.heyfix_worker.utils.Validations.fieldRegexEmail
import com.pandadevs.heyfix_worker.utils.Validations.fieldRegexName
import com.pandadevs.heyfix_worker.viewmodel.RegisterViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var google: GoogleSignInClient
    private lateinit var binding: ActivityRegisterBinding
    private var editsInputsList: List<TextInputLayout> = listOf()
    private var areCorrectFieldsList: MutableList<Boolean> = mutableListOf()
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        val options = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("61020705136-mufc3648s89a2ajcip1sd45e4r85p2of.apps.googleusercontent.com")
            .requestEmail()
            .build()
        google = GoogleSignIn.getClient(this, options)
        editsInputsList = listOf(
            binding.etName,
            binding.etFirstSurname,
            binding.etSecondSurname,
            binding.etWork, binding.etTransport,
            binding.etEmail,
            binding.etPhone,
            binding.etNewPassword,
            binding.etRepeatNewPassword
        )
        areCorrectFieldsList =
            mutableListOf(false, false, true, false, false, false, false, false, false)
        setListWorks()
        binding.tbApp.setNavigationOnClickListener { finish() }
        binding.btnRegister.setOnClickListener { checkFields() }
        binding.btnGoogle.setOnClickListener {
            google.signOut()
            google.silentSignIn()
            val intent = google.signInIntent
            getResult.launch(intent)
        }
        activeEventListenerOnEditText()
        initObservers()
    }

    fun setListWorks() {
        FirebaseFirestore
            .getInstance()
            .collection("categories")
            .get()
            .addOnSuccessListener { documents ->
                val items = mutableListOf<String>()
                documents.forEach { doc ->
                    items.add(doc.data["name"].toString())
                }
                (binding.etWork.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(items.toTypedArray())
            }
    }

    fun initObservers() {
        viewModel.result.observe(this) {
            SnackbarShow.showSnackbar(binding.root, it)
        }

        viewModel.error.observe(this) {
            SnackbarShow.showSnackbar(binding.root, it)
        }
    }

    suspend fun getIdCategory(name: String): String? {
        val response = CompletableDeferred<String?>()
        FirebaseFirestore.getInstance()
            .collection("categories")
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener {
                if (it.documents.size > 0) {
                    response.complete(it.documents[0].id)
                } else {
                    response.complete("nothing")
                }
            }
            .addOnFailureListener { response.complete("nothing") }
        return response.await()
    }

    private fun checkFields() {
        if (areCorrectFieldsList.none { !it } && checkPasswordsFields()) {
            LoadingScreen.show(this,"Registrando usuario...",false)
            lifecycleScope.launch {
                val name = binding.etName.editText?.text.toString()
                val firstSurname = binding.etFirstSurname.editText?.text.toString()
                val secondSurname = binding.etSecondSurname.editText?.text.toString()
                val user = UserPost(
                    name,
                    firstSurname,
                    secondSurname,
                    true,
                    false,
                    binding.etEmail.editText?.text.toString(),
                    binding.etPhone.editText?.text.toString(),
                    picture = "https://ui-avatars.com/api/?name=$name+$firstSurname&background=003543&color=fff&size=200",
                    ranked_avg = 0.0,
                    transport = binding.etTransport.editText?.text.toString(),
                    category_id = getIdCategory(binding.etWork.editText!!.text.toString())!!,
                    current_position = "",
                    last_online = Timestamp.now(),
                )
                viewModel.registerData(user, binding.etNewPassword.editText?.text.toString())
            }
            LoadingScreen.hide()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            editsInputsList.forEachIndexed { index, it ->
                if (!areCorrectFieldsList[index])
                    it.error = "* Requerido"
            }
        }
    }

    private fun checkPasswordsFields(): Boolean {
        val areCorrectPasswords =
            editsInputsList[7].editText?.text.toString() == editsInputsList[8].editText?.text.toString()
        if (!areCorrectPasswords) {
            editsInputsList[7].error = "* Requerido"
            editsInputsList[8].error = "* Requerido"
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

        binding.etFirstSurname.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[1] =
                fieldNotEmpty(editsInputsList[1], text.toString(), 2) && fieldRegexName(
                    editsInputsList[1],
                    text.toString()
                )
        }

        binding.etSecondSurname.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[2] =
                fieldRegexName(
                    editsInputsList[2],
                    text.toString()
                )
        }

        binding.etWork.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[3] = fieldNotEmpty(editsInputsList[3], text.toString(), 4)
        }

        binding.etTransport.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[4] = fieldNotEmpty(editsInputsList[4], text.toString(), 4)
        }

        binding.etEmail.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[5] =
                fieldNotEmpty(editsInputsList[5], text.toString()) && fieldRegexEmail(
                    editsInputsList[5],
                    text.toString()
                )
        }

        binding.etPhone.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[6] = fieldNotEmpty(editsInputsList[6], text.toString(), 10)
        }

        binding.etNewPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[7] = fieldNotEmpty(editsInputsList[7], text.toString())
        }

        binding.etRepeatNewPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[8] = fieldNotEmpty(editsInputsList[8], text.toString())
        }
    }


    fun registerWithGoogle(user: UserPost) {
        LoadingScreen.show(this,"",false)
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document()
            .set(user)
            .addOnSuccessListener {
                SnackbarShow.showSnackbar(binding.root, "Registro Exitoso")
                LoadingScreen.hide()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                LoadingScreen.hide()
                SnackbarShow.showSnackbar(binding.root, "El Registro de Datos fue Invalido")
            }
    }

    val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            var account = task.getResult(ApiException::class.java)
            var names = account.familyName.toString().split(" ")
            var data = UserPost(
                account.givenName.toString(),
                names[0],
                second_surname = if (names.size > 1) names[1] else "",
                true,
                false,
                account.email.toString(),
                phone_number = "",
                account.photoUrl.toString(),
                ranked_avg = 0.0,
                transport = "",
                category_id = "",
                current_position = "",
                last_online = Timestamp.now(),
            )
            registerWithGoogle(data)
        }
    }

}