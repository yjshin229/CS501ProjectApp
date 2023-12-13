package com.example.taskterriers.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.taskterriers.R
import com.example.taskterriers.databinding.FragmentSettingsBinding
import com.example.taskterriers.GoogleSignInActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment(){
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this)[SettingsViewModel::class.java]

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val intent: Intent = Intent(activity, GoogleSignInActivity::class.java)

        auth = FirebaseAuth.getInstance()

        //default_web_client_id error is an IDE error. leave it as it is.
        val gson = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = activity?.let { GoogleSignIn.getClient(it,gson) }!!

        binding.signOutButton.setOnClickListener{
            googleSignInClient.signOut()
            auth.signOut()
            startActivity(intent)
            activity?.finish()
        }

        setValues()

        binding.editButton.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_settings_to_settingsEditFragment)
        }
        val root: View = binding.root
        return root
    }

    fun setValues() {
        val sharedPreferences = activity?.getSharedPreferences("User", Context.MODE_PRIVATE)
        val userName = (sharedPreferences?.getString("username", null) ?: "")
        val userEmail = (sharedPreferences?.getString("email", null) ?: "")
        val major = (sharedPreferences?.getString("major", null) ?: "")
        val about = (sharedPreferences?.getString("about", null) ?: "")

        binding.userNameTextView.text = userName
        binding.userEmailTextView.text = userEmail
        binding.majorTextView.text = major
        binding.aboutTextView.text = about.trim()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}