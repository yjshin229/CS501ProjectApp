package com.example.taskterriers.ui.requests

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskterriers.R
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random

class RequestsViewModel : ViewModel() {

    private var db = Firebase.firestore

    private val _requests = MutableLiveData<List<RequestCardItem>>()
    val requests: LiveData<List<RequestCardItem>> = _requests
    private val firestoreRef = db.collection("requests").orderBy("createdAt", Query.Direction.DESCENDING)

    init {
        firestoreRef.get().addOnSuccessListener {documents ->
            val tempList = mutableListOf<RequestCardItem>()
            for(document in documents){
                val request = RequestCardItem(
                    id = document.id,
                    date = getCurrentDateTimeFormatted(document.getTimestamp("createdAt")?.toDate()),
                    name = document["requestName"].toString(),
                    chipString = document["requestType"].toString(),
                    contentPreview = if (document["requestDescription"].toString().length > 100) document["requestDescription"].toString().substring(0,100) else document["requestDescription"].toString(),
                    profileImageResId = R.drawable.ic_launcher_background,
                )
                tempList.add(request)
            }
            _requests.value = tempList
        }
    }

    fun refreshData() {
        firestoreRef.get().addOnSuccessListener { documents ->
            val tempList = mutableListOf<RequestCardItem>()
            for (document in documents) {
                val request = RequestCardItem(
                    id = document.id,
                    name = document["requestName"].toString(),
                    date = getCurrentDateTimeFormatted(document.getTimestamp("createdAt")?.toDate()),
                    chipString = document["requestType"].toString(),
                    contentPreview = if (document["requestDescription"].toString().length > 100) document["requestDescription"].toString().substring(0,100) else document["requestDescription"].toString() ,
                    profileImageResId = R.drawable.ic_launcher_background
                )
                tempList.add(request)
            }
            _requests.value = tempList
        }
    }
    private fun getCurrentDateTimeFormatted(date : Date?): String {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault())
        return dateFormat.format(date)
    }
}