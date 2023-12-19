package com.example.taskterriers.ui.messages

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskterriers.databinding.ReceiveMessageBoxBinding
import com.example.taskterriers.databinding.SentMessageBoxBinding
import com.google.firebase.auth.FirebaseAuth

class MessagesDetailAdapter(
    private val messageList: ArrayList<Message>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_RECEIVE = 1
    private val ITEM_SENT = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == ITEM_RECEIVE) {
            val binding = ReceiveMessageBoxBinding.inflate(inflater, parent, false)
            ReceiveViewHolder(binding)
        } else {
            val binding = SentMessageBoxBinding.inflate(inflater, parent, false)
            SentViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (holder is SentViewHolder) {
            holder.binding.sentTextMessage.text = currentMessage.message
        } else if (holder is ReceiveViewHolder) {
            holder.binding.receiveTextMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if (FirebaseAuth.getInstance().currentUser?.uid == currentMessage.sender) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int = messageList.size

    fun updateData(newMessages: List<Message>) {
        messageList.clear()
        messageList.addAll(newMessages)
        notifyDataSetChanged()
    }

    class SentViewHolder(val binding: SentMessageBoxBinding): RecyclerView.ViewHolder(binding.root)

    class ReceiveViewHolder(val binding: ReceiveMessageBoxBinding): RecyclerView.ViewHolder(binding.root)
}