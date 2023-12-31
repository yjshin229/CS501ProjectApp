package com.example.taskterriers.ui.requests

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskterriers.R
import com.example.taskterriers.databinding.FragmentRequestsBinding

private const val TAG = "RequestsFragment"
class RequestsFragment : Fragment() {

    private var _binding: FragmentRequestsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val requestsViewModel = ViewModelProvider(this)[RequestsViewModel::class.java]

        _binding = FragmentRequestsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.progressBar.visibility = View.VISIBLE

        binding.recyclerviewRequests.layoutManager = LinearLayoutManager(context)
        val requests =  requestsViewModel.requests
        val sharedPreferences = activity?.getSharedPreferences("User", Context.MODE_PRIVATE)
        val navController = findNavController()

        // Setting up the RecyclerView
        val adapter = RequestsCardAdapter(emptyList(), navController)
        binding.recyclerviewRequests.adapter = adapter

        requests.observe(viewLifecycleOwner) { requestsList ->
            (binding.recyclerviewRequests.adapter as RequestsCardAdapter).updateData(requestsList)
            binding.swipeRefreshLayout.isRefreshing = false
            binding.progressBar.visibility = View.GONE
            // Update adapter with new data
            adapter.updateData(requestsList)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            requestsViewModel.refreshData()
        }
        binding.addRequestsButton.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_requests_to_requestAddFragment)
        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
