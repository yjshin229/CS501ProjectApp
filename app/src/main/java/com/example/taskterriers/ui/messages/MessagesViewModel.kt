package com.example.taskterriers.ui.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskterriers.ui.requests.RequestCardItem
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MessagesViewModel : ViewModel() {

    private var db = Firebase.firestore

    private val _requests = MutableLiveData<List<RequestCardItem>>()
    val requests: LiveData<List<RequestCardItem>> = _requests
    private val firestoreRef = db.collection("chats").orderBy("updatedAt", Query.Direction.DESCENDING)


    private val _text = MutableLiveData<String>().apply {
        value = "This is messages Fragment"
    }
    val text: LiveData<String> = _text
}