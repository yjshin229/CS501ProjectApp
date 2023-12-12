package com.example.taskterriers.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.taskterriers.R
import com.example.taskterriers.databinding.ServicesCardBinding


class ServicesCardAdapter(
    private var services: List<ServiceCardItem>,
    private val navController: NavController
)
    : RecyclerView.Adapter<ServicesCardAdapter.ServiceHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ServiceHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ServicesCardBinding.inflate(inflater, parent, false)
        return ServiceHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceHolder, position: Int) {
        val service = services[position]
        holder.apply {
            binding.profileImageView.setImageResource(service.profileImageResId)
            binding.nameTextView.text = service.name
            binding.reusableChip.text = service.chipString
//            binding.numberOfReviews.text = service.numOfReview.toString()
//            binding.reviewDecimal.text = service.reviewDecimal
            binding.descriptionPreview.text = service.descriptionPreview
            binding.servicePrice.text = service.servicePrice.toString()
//            binding.buttonKebabMenu.setOnClickListener()
            binding.root.setOnClickListener{
                val actionId = R.id.action_navigation_services_to_serviceDetailFragment
                val bundle = Bundle().apply {
                    putString("serviceId", service.id)
                }
                navController.navigate(actionId, bundle)
            }

        }
    }

    class ServiceHolder(
        val binding: ServicesCardBinding,
    ): RecyclerView.ViewHolder(binding.root){
    }

    override fun getItemCount(): Int = services.size

    fun updateData(newServices: List<ServiceCardItem>) {
        services = newServices
        notifyDataSetChanged() // Notify the adapter of the data change
    }
}
