package com.example.taskterriers.ui.messages

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskterriers.R
import com.example.taskterriers.databinding.FragmentMessageDetailBinding
import com.example.taskterriers.databinding.FragmentRequestDetailBinding
import com.example.taskterriers.ui.requests.RequestsCardAdapter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MessageDetailFragment : Fragment() {
    private var _binding: FragmentMessageDetailBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private val firestoreUserRef = db.collection("users")
    private val firestoreChatRef = db.collection("chats")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessageDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = MessagesDetailAdapter(arrayListOf())
        binding.chatRecyclerView.adapter = adapter

        (activity as? AppCompatActivity)?.supportActionBar?.title = ""
        val chatId = arguments?.getString("chatId")
        val chatUserName = arguments?.getString("chatUserName")
        val chatUserId = arguments?.getString("chatUserId")

        if (chatId != null) {
            (activity as? AppCompatActivity)?.supportActionBar?.title = chatUserName
            if (chatUserId != null) {
                checkMessageHistory(chatUserId)
            }

            binding.sendMessageButton.setOnClickListener {
                sendMessage(chatId)
            }

            firestoreChatRef.document(chatId).collection("messages").orderBy("createdAt")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        // Handle error
                        return@addSnapshotListener
                    }

                    val messagesList = ArrayList<Message>()
                    for (doc in snapshot!!) {
                        val message = doc.toObject(Message::class.java)
                        messagesList.add(message)
                    }

                    // Update your RecyclerView
                    updateRecyclerView(messagesList)
                }
        }

        return root
    }

    private fun checkMessageHistory(uid: String){
        val sharedPreferences = activity?.getSharedPreferences("User", Context.MODE_PRIVATE)
        val myUid = (sharedPreferences?.getString("uid", null) ?: "")
        firestoreChatRef.whereArrayContains("users", uid).get().addOnSuccessListener { documents ->
            for (document in documents) {
                // This document's 'arrayField' contains 'desiredValue'
                // Handle each document
            }
        }
            .addOnFailureListener { exception ->
                // Handle any errors
            }
    }

    private fun sendMessage(chatId: String){
        val sharedPreferences = activity?.getSharedPreferences("User", Context.MODE_PRIVATE)
        val currentTime = Timestamp.now()
        val messageObject = Message(currentTime,binding.messageInputEditText.text.toString(),(sharedPreferences?.getString("uid", null) ?: ""))
        val newMessageInfo = hashMapOf(
            "message" to binding.messageInputEditText.text.toString(),
            "sender" to (sharedPreferences?.getString("uid", null) ?: ""),
            "createdAt" to currentTime,
        )
        firestoreChatRef.document(chatId).collection("messages").document().set(newMessageInfo).addOnSuccessListener {
            binding.messageInputEditText.text.clear()
        }
        firestoreChatRef.document(chatId).update("updatedAt", currentTime)

    }
    private fun updateRecyclerView(messages: ArrayList<Message>) {
        val adapter = binding.chatRecyclerView.adapter as? MessagesDetailAdapter
        adapter?.updateData(messages)
        binding.chatRecyclerView.scrollToPosition(messages.size - 1) // Scroll to the latest message
    }

}