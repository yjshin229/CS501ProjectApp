package com.example.taskterriers.ui.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.taskterriers.R
import com.example.taskterriers.databinding.RequestsCardBinding
import com.example.taskterriers.databinding.ServicesCardBinding
import com.example.taskterriers.ui.services.ServiceCardItem


class RequestsCardAdapter(
    private var requests: List<RequestCardItem>,
    private val navController: NavController
)
    : RecyclerView.Adapter<RequestsCardAdapter.RequestHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RequestHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RequestsCardBinding.inflate(inflater, parent, false)
        return RequestHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestHolder, position: Int) {
        val request = requests[position]
        holder.apply {
            binding.profileImageView.setImageResource(request.profileImageResId)
            binding.nameTextView.text = request.name
            binding.reusableChip.text = request.chipString
            binding.dateTextView.text = request.date
            binding.contentTextView.text = request.contentPreview
//            binding.buttonKebabMenu.setOnClickListener()
            binding.root.setOnClickListener{
                val actionId = R.id.action_navigation_requests_to_requestDetailFragment
                val bundle = Bundle().apply {
                    putString("requestId", request.id)
                }
                navController.navigate(actionId, bundle)
            }

        }
    }

    class RequestHolder(
        val binding: RequestsCardBinding,
    ): RecyclerView.ViewHolder(binding.root){

    }

    override fun getItemCount(): Int = requests.size

    fun updateData(newRequests: List<RequestCardItem>) {
        requests = newRequests
        notifyDataSetChanged() // Notify the adapter of the data change
    }
}
