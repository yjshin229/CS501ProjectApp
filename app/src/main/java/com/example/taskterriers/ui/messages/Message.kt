package com.example.taskterriers.ui.messages

import com.google.firebase.Timestamp

data class Message(
    val createdAt: Timestamp = Timestamp.now(),
    val sender: String = "",
    val message: String = ""
)
