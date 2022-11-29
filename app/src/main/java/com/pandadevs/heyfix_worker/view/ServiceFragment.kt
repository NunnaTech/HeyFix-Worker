package com.pandadevs.heyfix_worker.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pandadevs.heyfix_worker.databinding.FragmentServiceBinding
import com.pandadevs.heyfix_worker.utils.SharedPreferenceManager

class ServiceFragment : Fragment() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        initView()


        return binding.root
    }

    private fun initView() {
        val user = SharedPreferenceManager(requireContext()).getUser()
        binding.tvUser.text = user?.name
        binding.btnResolve.setOnClickListener {
            activity?.startActivity(Intent(context, RequestServiceActivity::class.java))
        }

        binding.btnGoOnService.setOnClickListener {
            activity?.startActivity(Intent(context, RequestServiceActivity::class.java))
        }

        binding.btnReject.setOnClickListener { confirmCancelService() }

        binding.plPulse.start()
    }


    private fun confirmCancelService() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("¿Cancelar solicitud?")
            .setMessage("Perderás esta oportunidad de trabajo")
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Sí, cancelar") { _, _ -> }
            .show()
    }

}