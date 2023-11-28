package com.example.taskterriers.ui.requests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskterriers.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random

class RequestsViewModel : ViewModel() {

    val requestTypes = arrayOf("Tutoring", "Move-in", "General", "Personal Care")
    val random = Random()
    val dateFormat = SimpleDateFormat("MM-dd-yyyy hh:mm a", Locale.getDefault())
    val calendar = Calendar.getInstance()

    val requests = mutableListOf<RequestItem>()
    init {
        for(i in 0 until 10){
            val request = RequestItem(
                name = "name$i",
                chipString = requestTypes[random.nextInt(4)],
                content = "content$i",
                date = dateFormat.format(calendar.time),
                numberOfComments = i + random.nextInt(20),
                profileImageResId = R.drawable.ic_launcher_background
            )
            requests += request
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}