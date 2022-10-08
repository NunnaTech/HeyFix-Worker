package com.pandadevs.heyfix_worker

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pandadevs.heyfix_worker.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.btnChangePass.setOnClickListener {
           activity?.startActivity(Intent(context, ChangePasswordActivity::class.java))
        }

        binding.btnAbout.setOnClickListener {
            activity?.startActivity(Intent(context, AboutActivity::class.java))
        }

        return binding.root
    }
}