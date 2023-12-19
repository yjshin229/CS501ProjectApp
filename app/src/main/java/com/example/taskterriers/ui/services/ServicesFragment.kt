package com.example.taskterriers.ui.services

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskterriers.R
import com.example.taskterriers.databinding.FragmentServicesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

        binding.progressBar.visibility = View.VISIBLE

        binding.recyclerViewServices.layoutManager = LinearLayoutManager(context)
        val services =  servicesViewModel.services
        val sharedPreferences = activity?.getSharedPreferences("User", Context.MODE_PRIVATE)
        val navController = findNavController()

        // Setting up the RecyclerView
        val adapter = ServicesCardAdapter(emptyList(), navController)
        binding.recyclerViewServices.adapter = adapter

        services.observe(viewLifecycleOwner) { servicesList ->
            (binding.recyclerViewServices.adapter as ServicesCardAdapter).updateData(servicesList)
            binding.swipeRefreshLayout.isRefreshing = false
            binding.progressBar.visibility = View.GONE
            // Update adapter with new data
            adapter.updateData(servicesList)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            servicesViewModel.refreshData()
        }

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