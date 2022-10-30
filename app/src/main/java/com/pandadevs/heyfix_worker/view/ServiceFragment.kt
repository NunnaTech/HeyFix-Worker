package com.pandadevs.heyfix_worker.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pandadevs.heyfix_worker.databinding.FragmentServiceBinding

class ServiceFragment : Fragment() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)


        binding.btnResolve.setOnClickListener {
            activity?.startActivity(Intent(context, RequestServiceActivity::class.java))
        }
        return binding.root
    }


}