package com.example.taskterriers.ui.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskterriers.R
import com.example.taskterriers.ui.requests.RequestItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random


class ServicesViewModel : ViewModel() {

    val serviceTypes = arrayOf("Tutoring", "Move-in", "General", "Personal Care")
    private val dateFormat = SimpleDateFormat("MM-dd-yyyy hh:mm a", Locale.getDefault())
    private val calendar: Calendar = Calendar.getInstance()

    val services = mutableListOf<ServiceCardItem>()
    init {
        for(i in 0 until 10){
            val service = ServiceCardItem(
                name = "name$i",
                chipString = serviceTypes[Random.nextInt(4)],
                descriptionPreview = "This is a description preview$i",
                numOfReview = Random.nextInt(20),
                servicePrice = String.format("%.2f",Random.nextDouble(0.0, 100.0)),
                reviewDecimal = String.format("%.2f",Random.nextDouble(0.0,5.0)),
                profileImageResId = R.drawable.ic_launcher_background
            )
            services += service
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}