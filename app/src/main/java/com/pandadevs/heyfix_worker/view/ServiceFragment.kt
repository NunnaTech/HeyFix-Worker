package com.pandadevs.heyfix_worker.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pandadevs.heyfix_worker.R
import com.pandadevs.heyfix_worker.databinding.FragmentServiceBinding
import com.pandadevs.heyfix_worker.provider.RequestServiceProvider
import com.pandadevs.heyfix_worker.utils.SharedPreferenceManager
import com.pandadevs.heyfix_worker.viewmodel.NotificationDataViewModel
import com.pandadevs.heyfix_worker.viewmodel.RequestServiceViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class ServiceFragment : Fragment() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!

    lateinit var requestServiceViewModel: RequestServiceViewModel

    private val notificationDataViewModel: NotificationDataViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        requestServiceViewModel = ViewModelProvider(this)[RequestServiceViewModel::class.java]
        initView()
        initObservers()
        return binding.root
    }


    private fun initView() {
        val user = SharedPreferenceManager(requireContext()).getUser()
        binding.tvUser.text = "!${user?.name}"
        binding.clNoWork.visibility = View.VISIBLE
        binding.clOnWork.visibility = View.INVISIBLE
        binding.llService.visibility = View.INVISIBLE
        binding.btnResolve.setOnClickListener { acceptService() }
        binding.btnGoOnService.setOnClickListener { goToRequestServiceActivity() }
        binding.btnReject.setOnClickListener { confirmCancelService() }
        binding.plPulse.start()

    }


    private fun goToRequestServiceActivity() {
        activity?.startActivity(Intent(context, RequestServiceActivity::class.java))
    }

    private fun acceptService() {

    }


    private fun initObservers() {

        notificationDataViewModel.notificationSelected.observe(viewLifecycleOwner) {
            if (it != null) {
                lifecycleScope.launch {
                    requestServiceViewModel.getClientData(it.client_id)
                }
                binding.tvDirection.text = it.address
                binding.clNoWork.visibility = View.INVISIBLE
                binding.clOnWork.visibility = View.INVISIBLE
                binding.llService.visibility = View.VISIBLE
            } else {
                binding.clNoWork.visibility = View.VISIBLE
                binding.clOnWork.visibility = View.INVISIBLE
                binding.llService.visibility = View.INVISIBLE
            }
        }

        requestServiceViewModel.clientData.observe(viewLifecycleOwner) {
            binding.tvClient.text = "${it.name} ${it.first_surname} ${it.second_surname}"
            Glide.with(requireContext()).load(it.picture).into(binding.civPicture)
        }

    }

    private fun confirmCancelService() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("¿Cancelar solicitud?")
            .setMessage("Perderás esta oportunidad de trabajo")
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Sí, cancelar") { _, _ ->
                RequestServiceProvider.cancelRequestService(notificationDataViewModel.notificationSelected.value!!.id)
                notificationDataViewModel.clearNotification()
                findNavController().navigate(R.id.action_itServices_to_itHome)
            }
            .show()
    }

}