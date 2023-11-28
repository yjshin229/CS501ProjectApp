package com.example.taskterriers.ui.requests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskterriers.databinding.RequestsCardBinding


class RequestsCardAdapter(private val requests: List<RequestItem>,
                          private val onItemClick: (RequestItem) -> Unit)
    : RecyclerView.Adapter<RequestsCardAdapter.RequestHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RequestHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RequestsCardBinding.inflate(inflater, parent, false)
        return RequestHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: RequestHolder, position: Int) {
        val request = requests[position]
        holder.apply {
            binding.profileImageView.setImageResource(request.profileImageResId)
            binding.nameTextView.text = request.name
            binding.reusableChip.text = request.chipString
            binding.commentTextView.text = request.numberOfComments.toString()
            binding.dateTextView.text = request.date.toString()
//            binding.buttonKebabMenu.setOnClickListener()
            binding.root.setOnClickListener{onItemClick(request)}

        }
    }

    class RequestHolder(
        val binding: RequestsCardBinding,
        val onItemClick: (RequestItem) -> Unit
    ): RecyclerView.ViewHolder(binding.root){

    }

    override fun getItemCount(): Int = requests.size
}
