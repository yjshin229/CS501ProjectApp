package com.example.taskterriers.ui.services

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.example.taskterriers.databinding.ServicesCardBinding

class ServicesCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private val binding: ServicesCardBinding

    init {
        val inflater = LayoutInflater.from(context)
        // Inflate using view binding
        binding = ServicesCardBinding.inflate(inflater, this, true)
    }
}
