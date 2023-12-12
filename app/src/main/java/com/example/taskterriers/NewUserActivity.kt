package com.example.taskterriers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskterriers.databinding.ActivityNewUserBinding
import com.example.taskterriers.databinding.ActivitySplashScreenBinding

class NewUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        val userName = (sharedPreferences?.getString("username", null) ?: "")
        val userEmail = (sharedPreferences?.getString("email", null) ?: "")

        binding.userNameTextView.text = userName
        binding.userEmailTextView.text = userEmail

        binding.saveButton.setOnClickListener {
            updateSharedPreferences()
            val intent: Intent = Intent(this,SplashScreen::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateSharedPreferences (){
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply{
            putString("about", binding.aboutEditText.text.toString())
            putString("major", binding.majorEditText.text.toString())
            apply()
        }

        sharedPreferences.getString("new about", sharedPreferences.getString("about", null))

    }
}