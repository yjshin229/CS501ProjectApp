package com.example.taskterriers.ui.services

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskterriers.databinding.ServicesCardBinding
import com.example.taskterriers.ui.services.ServiceCardItem


class ServicesCardAdapter(private var services: List<ServiceCardItem>,
                          private val onItemClick: (ServiceCardItem) -> Unit)
    : RecyclerView.Adapter<ServicesCardAdapter.ServiceHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ServiceHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ServicesCardBinding.inflate(inflater, parent, false)
        return ServiceHolder(binding, onItemClick)
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
            binding.root.setOnClickListener{onItemClick(service)}

        }
    }

    class ServiceHolder(
        val binding: ServicesCardBinding,
        val onItemClick: (ServiceCardItem) -> Unit
    ): RecyclerView.ViewHolder(binding.root){

    }

    override fun getItemCount(): Int = services.size

    fun updateData(newServices: List<ServiceCardItem>) {
        services = newServices
        notifyDataSetChanged() // Notify the adapter of the data change
    }
}
