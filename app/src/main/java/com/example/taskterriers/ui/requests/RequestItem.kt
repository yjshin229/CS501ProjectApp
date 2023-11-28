package com.example.taskterriers.ui.requests

data class RequestItem(
    val name: String,
    val date: String,
    val content: String,
    val chipString: String,
    val numberOfComments: Int,
    val profileImageResId: Int // Resource ID for the profile image
    // Add other fields as necessary
)
