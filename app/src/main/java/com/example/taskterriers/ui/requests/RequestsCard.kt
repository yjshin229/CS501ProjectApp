package com.example.taskterriers.ui.requests

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.example.taskterriers.databinding.RequestsCardBinding

class RequestsCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val binding: RequestsCardBinding

    init {
        val inflater = LayoutInflater.from(context)
        // Inflate using view binding
        binding = RequestsCardBinding.inflate(inflater, this, true)
    }

    fun setName(name: String) {
        binding.nameTextView.text = name
    }

    fun setDate(date: String) {
        binding.dateTextView.text = date
    }

    fun setContent(content: String) {
        binding.contentTextView.text = content
    }

    fun setComment(comment: String) {
        binding.commentTextView.text = comment
    }

    fun setProfileImage(resourceId: Int) {
        binding.profileImageView.setImageResource(resourceId)
    }

    fun setOnKebabMenuClickListener(listener: OnClickListener) {
        binding.buttonKebabMenu.setOnClickListener(listener)
    }
}
