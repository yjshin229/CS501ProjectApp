package com.example.taskterriers.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskterriers.R
import com.example.taskterriers.databinding.FragmentServicesBinding

class ServicesFragment : Fragment() {

    private var _binding: FragmentServicesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val servicesViewModel =
            ViewModelProvider(this)[ServicesViewModel::class.java]

        _binding = FragmentServicesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerViewServices.layoutManager = LinearLayoutManager(context)
        val services =  servicesViewModel.services

        // Setting up the RecyclerView
        val adapter = ServicesCardAdapter(services) { serviceItem ->
            // Handle click event for each item
            // For example, navigate to another fragment or activity
        }
        binding.recyclerViewServices.adapter = adapter

        binding.addServiceButton.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_services_to_addService)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}