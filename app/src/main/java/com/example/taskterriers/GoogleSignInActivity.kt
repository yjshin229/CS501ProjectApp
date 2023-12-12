package com.example.taskterriers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.taskterriers.databinding.ActivityGoogleSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class GoogleSignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoogleSignInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGoogleSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        //default_web_client_id error is an IDE error. leave it as it is.
        val gson = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gson)

        binding.googleSignInButton.setOnClickListener{
            signInWithGoogle()
        }

    }

    private fun signInWithGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
            if(result.resultCode == Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>){
        if(task.isSuccessful){
            val account: GoogleSignInAccount? = task.result
            if(account != null){
                updateUI(account)
            }

        }else{
            Toast.makeText(
                this,
                task.exception.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if (it.isSuccessful){
                val domain: String? = account.email?.substringAfterLast("@")
                if(domain != ("bu.edu")){
                    googleSignInClient.signOut()
                    Toast.makeText(
                        this,
                         account.email + " is not a bu email. Please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    val intent: Intent = Intent(this,MainActivity::class.java)
                    val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.apply{
                        putString("username", auth.currentUser?.displayName.toString())
                        putString("uid", auth.currentUser?.uid.toString())
                        putString("email", auth.currentUser?.email.toString())
                        apply()
                    }
                    startActivity(intent)
                    finish()
                }

            }else{
                Toast.makeText(
                    this,
                    it.exception.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}