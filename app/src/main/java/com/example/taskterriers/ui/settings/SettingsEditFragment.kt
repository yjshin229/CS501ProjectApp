package com.example.taskterriers.ui.settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.taskterriers.R
import com.example.taskterriers.databinding.FragmentServiceDetailBinding
import com.example.taskterriers.databinding.FragmentSettingsEditBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SettingsEditFragment : Fragment() {
    private var _binding: FragmentSettingsEditBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                (activity as? AppCompatActivity)?.supportActionBar?.title = "Edit User Information"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsEditBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initValues()
        return root
    }

    private fun initValues() {
            val sharedPreferences = activity?.getSharedPreferences("User", Context.MODE_PRIVATE)
            val userName = (sharedPreferences?.getString("username", null) ?: "")
            val userEmail = (sharedPreferences?.getString("email", null) ?: "")
            val major = (sharedPreferences?.getString("major", null) ?: "")
            val about = (sharedPreferences?.getString("about", null) ?: "")

            binding.userNameTextView.text = userName
            binding.userEmailTextView.text = userEmail
            binding.majorEditText.setText(major)
            binding.aboutEditText.setText(about)

        binding.saveButton.setOnClickListener {
            updateValues()
            val fm : FragmentManager = requireActivity().supportFragmentManager
            fm.popBackStack()
        }
    }

    private fun updateValues(){
        val sharedPreferences = activity?.getSharedPreferences("User", Context.MODE_PRIVATE)
        val uid = (sharedPreferences?.getString("uid", null) ?: "")

        val editor = sharedPreferences?.edit()
        editor?.apply{
            putString("about", binding.aboutEditText.text.toString())
            putString("major", binding.majorEditText.text.toString())
            apply()
        }

        db.collection("users").document(uid).update("major", binding.majorEditText.text.toString(), "about",binding.aboutEditText.text.toString())
    }

}