package com.example.taskterriers.ui.services

data class ServiceCardItem(
    val id: String,
    val name: String,
    val descriptionPreview: String,
    val chipString: String,
    val servicePrice: Int,
    val profileImageResId: Int
)
