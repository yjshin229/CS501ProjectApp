package com.example.taskterriers.ui.services

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskterriers.R
import com.example.taskterriers.ui.requests.RequestItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random


class ServicesViewModel : ViewModel() {

    val serviceTypes = arrayOf("Tutoring", "Move-in", "General", "Personal Care")
    private val dateFormat = SimpleDateFormat("MM-dd-yyyy hh:mm a", Locale.getDefault())
    private val calendar: Calendar = Calendar.getInstance()
    private var db = Firebase.firestore

    private val _services = MutableLiveData<List<ServiceCardItem>>()
    val services: LiveData<List<ServiceCardItem>> = _services
    private val firestoreRef = db.collection("services")

    init {
        firestoreRef.get().addOnSuccessListener {documents ->
            val tempList = mutableListOf<ServiceCardItem>()
            for(document in documents){
                val service = ServiceCardItem(
                    name = document["serviceName"].toString(),
                    chipString = document["serviceType"].toString(),
                    descriptionPreview = document["serviceDescription"].toString().substring(0,100),
                    servicePrice = document["serviceRate"].toString().toInt(),
                    profileImageResId = R.drawable.ic_launcher_background
                )
                tempList.add(service)
                Log.d("serviceName", document["serviceName"].toString())
            }
            _services.value = tempList
        }
    }
    fun refreshData() {
        firestoreRef.get().addOnSuccessListener { documents ->
            val tempList = mutableListOf<ServiceCardItem>()
            for (document in documents) {
                val service = ServiceCardItem(
                    name = document["serviceName"].toString(),
                    chipString = document["serviceType"].toString(),
                    descriptionPreview = document["serviceDescription"].toString().substring(0,100),
                    servicePrice = document["serviceRate"].toString().toInt(),
                    profileImageResId = R.drawable.ic_launcher_background
                )
                tempList.add(service)
            }
            _services.value = tempList
        }
    }

}