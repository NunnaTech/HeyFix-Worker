package com.pandadevs.heyfix_worker.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.GeoPoint
import com.pandadevs.heyfix_worker.R
import com.pandadevs.heyfix_worker.data.model.UserGet
import com.pandadevs.heyfix_worker.databinding.FragmentServiceBinding
import com.pandadevs.heyfix_worker.provider.RequestServiceProvider
import com.pandadevs.heyfix_worker.utils.SharedPreferenceManager
import com.pandadevs.heyfix_worker.utils.SnackbarShow
import com.pandadevs.heyfix_worker.viewmodel.RequestServiceViewModel
import kotlinx.coroutines.launch

class ServiceFragment : Fragment() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!
    private val requestServiceViewModel: RequestServiceViewModel by activityViewModels()
    private var nameClient: String = ""
    private var addressClient: String = ""
    private var idClient: String = ""
    private var idRequestService: String = ""
    lateinit var user: UserGet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        initView()
        initObservers()
        return binding.root
    }

    private fun initView() {
        user = SharedPreferenceManager(requireContext()).getUser()!!
        binding.clNoWork.visibility = View.VISIBLE
        binding.llService.visibility = View.INVISIBLE
        binding.tvUser.text = "¡${user?.name}"
        binding.btnResolve.setOnClickListener { acceptService() }
        binding.btnReject.setOnClickListener { confirmCancelService() }
        binding.plPulse.start()
    }

    private fun initObservers() {
        requestServiceViewModel.isThereACurrentRequest.observe(viewLifecycleOwner) {
            if (it != null && it.accepted == "") {
                idClient = it.client_id
                idRequestService = it.id
                lifecycleScope.launch { requestServiceViewModel.getClientData(idClient) }
                addressClient = it.address
                binding.tvDirection.text = addressClient
                binding.clNoWork.visibility = View.INVISIBLE
                binding.llService.visibility = View.VISIBLE
            } else goToHomeFragment()
        }

        requestServiceViewModel.clientData.observe(viewLifecycleOwner) {
            nameClient = "${it.name} ${it.first_surname} ${it.second_surname}"
            binding.tvClient.text = nameClient
            Glide.with(requireContext()).load(it.picture).into(binding.civPicture)
        }

        requestServiceViewModel.isClientAvailable.observe(viewLifecycleOwner) {
            if (it) takeService()
            else {
                SnackbarShow.showSnackbar(binding.root, "El cliente ya no está disponible")
                goToHomeFragment()
            }
        }

        requestServiceViewModel.takeServiceCompleted.observe(viewLifecycleOwner) {
            if (it) goToRequestServiceActivity()
        }
    }


    private fun goToRequestServiceActivity() {
        val intent = Intent(context, RequestServiceActivity::class.java)
        intent.putExtra(RequestServiceActivity.ID_SERVICE_HIRED, idRequestService)
        activity?.startActivity(intent)
    }

    private fun acceptService() {
        lifecycleScope.launch { requestServiceViewModel.isClientAvailable(idClient) }
    }

    private fun takeService() {
        lifecycleScope.launch {
            val user = SharedPreferenceManager(requireContext()).getUser()
            requestServiceViewModel.takeService(
                requestServiceViewModel.isThereACurrentRequest.value!!,
                GeoPoint(MainActivity.LATITUD_USER, MainActivity.LONGITUDE_USER),
                user?.category_id!!,
                "${user.name} ${user.first_surname} ${user.second_surname}",
                nameClient,
                addressClient
            )
        }
    }

    private fun confirmCancelService() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("¿Cancelar solicitud?")
            .setMessage("Perderás esta oportunidad de trabajo")
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Sí, cancelar") { _, _ -> cancelService() }
            .show()
    }

    private fun goToHomeFragment() {
        binding.clNoWork.visibility = View.VISIBLE
        binding.llService.visibility = View.INVISIBLE
    }

    private fun cancelService() {
        RequestServiceProvider.cancelRequestService(idRequestService)
        goToHomeFragment()
    }

}