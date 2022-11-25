package com.pandadevs.heyfix_worker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pandadevs.heyfix_worker.data.model.UserGet
import com.pandadevs.heyfix_worker.databinding.FragmentHomeBinding
import com.pandadevs.heyfix_worker.utils.SharedPreferenceManager
import com.pandadevs.heyfix_worker.utils.TimeDay

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView() {
        val user: UserGet? = SharedPreferenceManager(this.requireContext()).getUser()
        binding.tvName.text = user?.name
        binding.tvWelcome.text = TimeDay.getTime()
        binding.tvRate.text = user?.ranked_avg.toString()
    }

}