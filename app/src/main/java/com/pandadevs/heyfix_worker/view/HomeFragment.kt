package com.pandadevs.heyfix_worker.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pandadevs.heyfix_worker.R
import com.pandadevs.heyfix_worker.adapters.HistorialServiceAdapter
import com.pandadevs.heyfix_worker.data.model.UserGet
import com.pandadevs.heyfix_worker.databinding.FragmentHomeBinding
import com.pandadevs.heyfix_worker.utils.SharedPreferenceManager
import com.pandadevs.heyfix_worker.utils.TimeDay
import com.pandadevs.heyfix_worker.viewmodel.HistorialServicesViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val historialServicesViewModel: HistorialServicesViewModel by activityViewModels()
    var adapter: HistorialServiceAdapter? = null;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initView()
        initObservers()
        return binding.root
    }

    private fun initObservers() {
        historialServicesViewModel.historialServices.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                binding.rvHistorialServices.layoutManager = LinearLayoutManager(requireActivity())
                adapter = HistorialServiceAdapter(requireActivity())
                binding.rvHistorialServices.adapter = adapter
                adapter!!.submitList(it)
                adapter!!.notifyDataSetChanged()
                binding.tvNoHistorialServices.visibility = View.GONE
            } else {
                binding.tvNoHistorialServices.visibility = View.VISIBLE
                binding.rvHistorialServices.visibility = View.GONE
            }
        }
        historialServicesViewModel.rateServices.observe(viewLifecycleOwner) {
            binding.tvRate.text = it.toString().substring(0, 3)
        }
    }

    private fun initView() {
        val user: UserGet? = SharedPreferenceManager(this.requireContext()).getUser()
        binding.tvName.text = user?.name
        binding.tvWelcome.text = TimeDay.getTime()
        if (user?.category_id == "" || user?.phone_number=="") binding.mcvCompleteProfile.visibility = View.VISIBLE
        else binding.mcvCompleteProfile.visibility = View.GONE
        lifecycleScope.launch { historialServicesViewModel.getHistorialServices(user!!.id)}
    }
}