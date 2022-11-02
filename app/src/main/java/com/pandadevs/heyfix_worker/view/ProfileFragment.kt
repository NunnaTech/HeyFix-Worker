package com.pandadevs.heyfix_worker.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.pandadevs.heyfix_worker.databinding.FragmentProfileBinding
import com.pandadevs.heyfix_worker.utils.SnackbarShow
import com.pandadevs.heyfix_worker.utils.Validations.fieldNotEmpty
import com.pandadevs.heyfix_worker.utils.Validations.fieldRegexName

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var editsInputsList: List<TextInputLayout> = listOf()
    private var areCorrectFieldsList: MutableList<Boolean> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        editsInputsList = listOf(binding.etName, binding.etLastName, binding.etWork, binding.etTransport, binding.etPhone)
        areCorrectFieldsList = mutableListOf(false, false, false, false, false)

        binding.btnChangePass.setOnClickListener { goToActivity(ChangePasswordActivity::class.java) }
        binding.btnAbout.setOnClickListener { goToActivity(AboutActivity::class.java) }
        binding.btnSave.setOnClickListener { checkFields() }

        activeEventListenerOnEditText()

        return binding.root
    }

    private fun goToActivity(cls: Class<*>) {
        activity?.startActivity(Intent(context, cls))
    }

    private fun checkFields() {
        if (areCorrectFieldsList.none { !it }) {
            SnackbarShow.showSnackbar(binding.root, "Guardado exitoso")
        } else {
            editsInputsList.forEachIndexed { index, it ->
                if (!areCorrectFieldsList[index]) it.error = "* Requerido"
            }
        }
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

        binding.etPhone.editText?.doOnTextChanged { text, _, _, _ ->
            areCorrectFieldsList[4] = fieldNotEmpty(editsInputsList[4], text.toString(), 10)
        }
    }
}