package com.example.taskterriers.ui.messages

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskterriers.R
import com.example.taskterriers.ui.requests.RequestCardItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessagesViewModel : ViewModel() {

    private var db = Firebase.firestore

    private val _chats = MutableLiveData<List<ChatCardItem>>()
    val chats: LiveData<List<ChatCardItem>> = _chats
    private val firestoreRef = db.collection("chats").orderBy("updatedAt", Query.Direction.DESCENDING)
    private val myUid = FirebaseAuth.getInstance().currentUser!!.uid
    init {
        firestoreRef.get().addOnSuccessListener {documents ->
            val tempList = mutableListOf<ChatCardItem>()
            for(document in documents){
                val usersList = document["users"] as? List<String> ?: listOf()
                val otherUser = getOtherUser(usersList, myUid)
                val chat = ChatCardItem(
                    id = document.id,
                    updatedAt = getCurrentDateTimeFormatted(document.getTimestamp("updatedAt")?.toDate()),
                    chatName = otherUser,
                )
                tempList.add(chat)
            }
            _chats.value = tempList
        }
    }

    fun refreshData() {
        firestoreRef.get().addOnSuccessListener { documents ->
            val tempList = mutableListOf<ChatCardItem>()
            for (document in documents) {
                val usersList = document["users"] as? List<String> ?: listOf()
                val otherUser = getOtherUser(usersList, myUid)
                val request = ChatCardItem(
                    id = document.id,
                    updatedAt = getCurrentDateTimeFormatted(document.getTimestamp("updatedAt")?.toDate()),
                    chatName = otherUser,
                )
                tempList.add(request)
            }
            _chats.value = tempList
        }
    }
    private fun getCurrentDateTimeFormatted(date : Date?): String {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault())
        return dateFormat.format(date)
    }

    private fun getOtherUser(users: List<String>, myUid: String): String {
        return if (users[0] == myUid) users[1] else users[0]
    }
}