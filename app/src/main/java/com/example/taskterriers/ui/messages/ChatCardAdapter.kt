package com.example.taskterriers.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.taskterriers.R
import com.example.taskterriers.databinding.ChatCardBinding


class ChatCardAdapter(
    private var chats: List<ChatCardItem>,
    private val navController: NavController
)
    : RecyclerView.Adapter<ChatCardAdapter.ChatHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ChatHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ChatCardBinding.inflate(inflater, parent, false)
        return ChatHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        val chat = chats[position]
        holder.apply {
            binding.nameTextView.text = chat.chatName
            binding.dateTextView.text = chat.updatedAt
            binding.root.setOnClickListener{
                val actionId = R.id.action_navigation_messages_to_messageDetailFragment
                val bundle = Bundle().apply {
                    putString("chatId", chat.id)
                    putString("chatUserName", chat.chatName)
                    putString("chatUserId", chat.chatUserId)
                }
                navController.navigate(actionId, bundle)
            }

        }
    }

    class ChatHolder(
        val binding: ChatCardBinding,
    ): RecyclerView.ViewHolder(binding.root){

    }

    override fun getItemCount(): Int = chats.size

    fun updateData(newChats: List<ChatCardItem>) {
        chats = newChats
        notifyDataSetChanged() // Notify the adapter of the data change
    }
}
