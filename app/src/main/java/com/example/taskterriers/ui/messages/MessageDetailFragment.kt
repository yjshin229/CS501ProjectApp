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
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MessageDetailFragment : Fragment() {
    private var _binding: FragmentMessageDetailBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private val firestoreUserRef = db.collection("users")
    private val firestoreChatRef = db.collection("chats")

    private var fetchedChatId = ""

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

        (activity as? AppCompatActivity)?.supportActionBar?.title = chatUserName
        if (chatId != null ) {

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
        }else if (chatUserId != null) {
            checkMessageHistory(chatUserId, object : ChatIdCallback {
                override fun onChatIdFound(chatId: String) {
                    fetchedChatId = chatId
                    // Now fetch messages using this chatId
                    fetchMessages(chatId)

                }

                override fun onNoChatIdFound() {

                }

                override fun onError(e: Exception) {
                    Log.e("MessageDetailFragment", "Error fetching chat ID", e)
                    // Handle error
                }
            })
        }
        binding.sendMessageButton.setOnClickListener {
            sendMessage(chatUserId!!, chatUserName!!)
        }

        return root
    }

    interface ChatIdCallback {
        fun onChatIdFound(chatId: String)
        fun onNoChatIdFound()
        fun onError(e: Exception)
    }
    private fun checkMessageHistory(uid: String, callback: ChatIdCallback): String {
        val sharedPreferences = activity?.getSharedPreferences("User", Context.MODE_PRIVATE)
        val myUid = (sharedPreferences?.getString("uid", null) ?: "")

        val firstQuery = firestoreChatRef.whereArrayContains("users", uid).get()
        val secondQuery = firestoreChatRef.whereArrayContains("users", myUid).get()

        var documentId = ""
        Tasks.whenAllSuccess<DocumentSnapshot>(firstQuery, secondQuery)
            .addOnSuccessListener { results ->
                val firstResultSet = (results[0] as QuerySnapshot).documents.map { it.id }.toSet()
                val secondResultSet = (results[1] as QuerySnapshot).documents.map { it.id }.toSet()

                // Find intersection of both sets of IDs
                val commonDocumentIds = firstResultSet.intersect(secondResultSet)
                if (commonDocumentIds.isNotEmpty()) {
                    callback.onChatIdFound(commonDocumentIds.first()) // Pass the first common ID
                }else{
                    callback.onNoChatIdFound()
                }
                for (id in commonDocumentIds) {
                    documentId = id
                }
            }
            .addOnFailureListener { exception ->
                callback.onError(exception)
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }

        return documentId
    }

    private fun fetchMessages(chatId: String) {
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
                updateRecyclerView(messagesList)
            }
    }
    private fun sendMessage(chatUserId: String, chatUserName: String) {
        checkMessageHistory(chatUserId, object : ChatIdCallback {
            override fun onChatIdFound(chatId: String) {
                // Chat room exists, send message to this chatId
                sendToExistingChatRoom(chatId)
            }

            override fun onError(e: Exception) {
                // Handle error (e.g., show a message to the user)
            }

            override fun onNoChatIdFound() {
                // No existing chat room, create a new one
                createNewChatRoom(chatUserId, chatUserName) { newChatId ->
                    sendToExistingChatRoom(newChatId)
                }
            }
        })
    }

    private fun createNewChatRoom(userId: String, userName: String, callback: (String) -> Unit) {
        // Logic to create a new chat room
        val sharedPreferences = activity?.getSharedPreferences("User", Context.MODE_PRIVATE)
        val myUid = (sharedPreferences?.getString("uid", null) ?: "")
        val myUserName = (sharedPreferences?.getString("userName", null) ?: "")
        val newChatroomInfo = hashMapOf(
            "users" to listOf(myUid, userId),
            "userNames" to listOf(myUserName, userName),
            "createdAt" to Timestamp.now(),
            "updatedAt" to Timestamp.now()
        )
        val newChatroomRef = firestoreChatRef.document()
        newChatroomRef.set(newChatroomInfo).addOnSuccessListener {
            val newChatId = newChatroomRef.id // Replace with actual chat ID generation logic
            callback(newChatId)
        }.addOnFailureListener { e ->
            // Handle any failure in setting the document
            Log.e("Firestore", "Error setting new chatroom: ", e)
        }

    }

    private fun sendToExistingChatRoom(chatId: String){
        val sharedPreferences = activity?.getSharedPreferences("User", Context.MODE_PRIVATE)
        val currentTime = Timestamp.now()
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