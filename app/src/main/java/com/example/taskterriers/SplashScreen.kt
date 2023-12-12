package com.example.taskterriers

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.taskterriers.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
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
            db.collection("users").document(auth.currentUser?.uid.toString()).get().addOnCompleteListener {task->
                if(task.isSuccessful){
                    val data = task.result
                    val about: String = data.get("about").toString()
                    val major: String = data.get("major").toString()
                    editor.apply{
                        putString("username", auth.currentUser?.displayName.toString())
                        putString("uid", auth.currentUser?.uid.toString())
                        putString("email", auth.currentUser?.email.toString())
                        putString("major", major)
                        putString("about", about)
                        apply()
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(intent)
                        finish()
                    },  1000)
                }
            }


        }else{
            val intent: Intent = Intent(this, GoogleSignInActivity::class.java)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
                finish()
            }, 1000)
        }
    }
}