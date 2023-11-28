package com.example.taskterriers.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskterriers.databinding.FragmentRequestsBinding
import com.example.taskterriers.databinding.FragmentServicesBinding
import com.example.taskterriers.ui.requests.RequestsCardAdapter

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

//        binding..layoutManager = LinearLayoutManager(context)
//        val requests =  requestsViewModel.requests
//
//        // Setting up the RecyclerView
//        val adapter = RequestsCardAdapter(requests) { requestItem ->
//            // Handle click event for each item
//            // For example, navigate to another fragment or activity
//        }
//        binding.recyclerviewRequests.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}