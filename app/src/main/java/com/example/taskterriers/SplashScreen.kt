package com.example.taskterriers

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.taskterriers.databinding.ActivitySplashScreenBinding
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
            val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            Log.d("SP username", auth.currentUser?.displayName.toString())
            Log.d("SP uid", auth.currentUser?.uid.toString())
            Log.d("SP email", auth.currentUser?.email.toString())
            editor.apply{
                putString("username", auth.currentUser?.displayName.toString())
                putString("uid", auth.currentUser?.uid.toString())
                putString("email", auth.currentUser?.email.toString())
                apply()
            }
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
                finish()
            },  1000)
        }else{
            val intent: Intent = Intent(this, GoogleSignInActivity::class.java)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
                finish()
            }, 1000)
        }
    }
}