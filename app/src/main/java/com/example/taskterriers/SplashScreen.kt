package com.example.taskterriers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskterriers.databinding.ActivityGoogleSignInBinding
import com.example.taskterriers.databinding.ActivitySplashScreenBinding
import com.example.taskterriers.ui.authentication.GoogleSignInActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null){
            updateUI(true)
        }else{
            updateUI(false)
        }
    }

    private fun updateUI(currentUser: Boolean){
        if(currentUser){
            val intent: Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val intent: Intent = Intent(this,GoogleSignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}