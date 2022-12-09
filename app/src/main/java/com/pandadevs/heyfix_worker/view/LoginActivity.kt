package com.pandadevs.heyfix_worker.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.pandadevs.heyfix_worker.data.model.UserGet
import com.pandadevs.heyfix_worker.databinding.ActivityLoginBinding
import com.pandadevs.heyfix_worker.utils.LoadingScreen
import com.pandadevs.heyfix_worker.utils.SharedPreferenceManager
import com.pandadevs.heyfix_worker.utils.SnackbarShow
import com.pandadevs.heyfix_worker.utils.Validations.fieldNotEmpty
import com.pandadevs.heyfix_worker.utils.Validations.fieldRegexEmail
import com.pandadevs.heyfix_worker.viewmodel.LoginViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import java.lang.Boolean.parseBoolean
import java.lang.Double.parseDouble

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var editsInputsList: List<TextInputLayout> = listOf()
    private var areCorrectFieldsList: MutableList<Boolean> = mutableListOf()
    private lateinit var viewModel: LoginViewModel
    private lateinit var google: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        editsInputsList = listOf(binding.etEmail, binding.etPassword)
        requestLocationPermission()
        val options = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("61020705136-mufc3648s89a2ajcip1sd45e4r85p2of.apps.googleusercontent.com")
            .requestEmail()
            .build()
        google = GoogleSignIn.getClient(this, options)
        areCorrectFieldsList = mutableListOf(false, false)
        binding.btnRegister.setOnClickListener { goToActivity(RegisterActivity::class.java) }
        binding.btnLogin.setOnClickListener { checkFields() }
        binding.btnGoogle.setOnClickListener {
            google.signOut()
            google.silentSignIn()
            val intent = google.signInIntent
            getResult.launch(intent)
        }
        activeEventListenerOnEditText()
        initObservers()
    }

    suspend fun existUser(email: String): Boolean? {
        val response = CompletableDeferred<Boolean?>()
        FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener {
                if (it.documents.size > 0) {
                    response.complete(true)
                } else {
                    response.complete(false)
                }
            }
            .addOnFailureListener { response.complete(false) }
        return response.await()
    }

    private fun checkFields() {
        if (areCorrectFieldsList.none { !it }) {
            lifecycleScope.launch {
                viewModel.loginEmail(
                    binding.etEmail.editText?.text.toString(),
                    binding.etPassword.editText?.text.toString()
                )
            }
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
            areCorrectFieldsList[0] =
                fieldNotEmpty(editsInputsList[0], text.toString()) && fieldRegexEmail(
                    editsInputsList[0],
                    text.toString()
                )
        }

        binding.etPassword.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[1] = fieldNotEmpty(editsInputsList[1], text.toString())
        }
    }

    fun initObservers() {
        viewModel.result.observe(this) {
            getDataToPreferences("email", null)
        }

        viewModel.error.observe(this) {
            SnackbarShow.showSnackbar(
                binding.root,
                "Error al iniciar sesión, los datos son incorrectos"
            )
        }
    }

    fun getDataToPreferences(method: String, userEmailGoogle: String?) {
        val email =
            if (method == "email") binding.etEmail.editText?.text.toString() else userEmailGoogle
        LoadingScreen.show(this,"Iniciando Sesión...",false)
        FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                val user = UserGet(
                    documents.documents[0].id,
                    documents.documents[0].data!!["name"].toString(),
                    documents.documents[0].data!!["first_surname"].toString(),
                    documents.documents[0].data!!["second_surname"].toString(),
                    parseBoolean(documents.documents[0].data!!["active"].toString()),
                    parseBoolean(documents.documents[0].data!!["client"].toString()),
                    documents.documents[0].data!!["email"].toString(),
                    documents.documents[0].data!!["phone_number"].toString(),
                    documents.documents[0].data!!["picture"].toString(),
                    parseDouble(documents.documents[0].data!!["ranked_avg"].toString()),
                    documents.documents[0].data!!["transport"].toString(),
                    documents.documents[0].data!!["category_id"].toString(),
                    documents.documents[0].data!!["current_position"].toString(),
                )
                SharedPreferenceManager(applicationContext).saveProviderMail(method)
                SharedPreferenceManager(applicationContext).saveUID(FirebaseAuth.getInstance().currentUser!!.uid)
                SharedPreferenceManager(this).saveUser(user)
                SharedPreferenceManager(this).saveSession()
                LoadingScreen.hide()
                startActivity(Intent(this, MainActivity::class.java))
            }
    }

    val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            val account = task.getResult(ApiException::class.java)
            val credenciales = GoogleAuthProvider
                .getCredential(account.idToken, null)
            lifecycleScope.launch {
                if (existUser(account.email!!) == true) {
                    FirebaseAuth.getInstance()
                        .signInWithCredential(credenciales)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                getDataToPreferences("google", account.email)
                            } else {
                                Snackbar.make(
                                    binding.root,
                                    "No existe una cuenta vinculada",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Snackbar.make(
                        binding.root,
                        "No existe una cuenta vinculada",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            SnackbarShow.showSnackbar(binding.root, "Ve ajustes a dar permisos de localización")
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        }
    }
}