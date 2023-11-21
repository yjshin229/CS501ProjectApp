package com.example.taskterriers.ui.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskterriers.R
import com.example.taskterriers.databinding.FragmentRequestsBinding

class RequestsFragment : Fragment() {

    private var _binding: FragmentRequestsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val requestsViewModel = ViewModelProvider(this).get(RequestsViewModel::class.java)

        _binding = FragmentRequestsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Example list of RequestItems
        val exampleList = listOf(
            RequestItem("Name 1", "Date 1", "Content 1", R.drawable.ic_launcher_background), // Replace with actual data and drawable
            RequestItem("Name 2", "Date 2", "Content 2", R.drawable.ic_launcher_background),
            RequestItem("Name 3", "Date 3", "Content 3", R.drawable.ic_launcher_background),
            RequestItem("Name 4", "Date 4", "Content 4", R.drawable.ic_launcher_background),
            RequestItem("Name 5", "Date 5", "Content 5", R.drawable.ic_launcher_background),
            RequestItem("Name 6", "Date 6", "Content 6", R.drawable.ic_launcher_background),
            RequestItem("Name 7", "Date 7", "Content 7", R.drawable.ic_launcher_background),
            RequestItem("Name 8", "Date 8", "Content 8", R.drawable.ic_launcher_background),
            RequestItem("Name 9", "Date 9", "Content 9", R.drawable.ic_launcher_background),
            RequestItem("Name 10", "Date 10", "Content 10", R.drawable.ic_launcher_background),
            // Add more items...
        )

        // Setting up the RecyclerView
        val adapter = RequestsCardAdapter(exampleList) { requestItem ->
            // Handle click event for each item
            // For example, navigate to another fragment or activity
        }

        binding.recyclerviewRequests.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
