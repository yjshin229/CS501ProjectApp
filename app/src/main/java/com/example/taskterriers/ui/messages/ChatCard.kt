package com.example.taskterriers.ui.messages

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.example.taskterriers.databinding.ChatCardBinding

class ChatCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val binding: ChatCardBinding

    init {
        val inflater = LayoutInflater.from(context)
        // Inflate using view binding
        binding = ChatCardBinding.inflate(inflater, this, true)
    }
}
