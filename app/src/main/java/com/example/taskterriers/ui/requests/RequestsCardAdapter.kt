package com.example.taskterriers.ui.requests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskterriers.databinding.RequestsCardBinding

class RequestsCardAdapter(private val items: List<RequestItem>,
                          private val onItemClick: (RequestItem) -> Unit)
    : RecyclerView.Adapter<RequestsCardAdapter.ViewHolder>() {

    class ViewHolder(private val binding: RequestsCardBinding,
                     private val onItemClick: (RequestItem) -> Unit)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RequestItem) {
            with(binding) {
                profileImageView.setImageResource(item.profileImageResId)
                nameTextView.text = item.name
                dateTextView.text = item.date
                contentTextView.text = item.content
                // You can setup click listener for buttonKebabMenu as well
                root.setOnClickListener { onItemClick(item) }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RequestsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
