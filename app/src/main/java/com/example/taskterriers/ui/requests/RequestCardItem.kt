package com.example.taskterriers.ui.requests

data class RequestCardItem(
    val id: String,
    val name: String,
    val date: String,
    val chipString: String,
    val contentPreview: String,
    val profileImageResId: Int // Resource ID for the profile image
    // Add other fields as necessary
)
