package com.example.taskterriers.ui.messages

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskterriers.R
import com.example.taskterriers.databinding.FragmentMessageDetailBinding
import com.example.taskterriers.databinding.FragmentRequestDetailBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MessageDetailFragment : Fragment() {
    private var _binding: FragmentMessageDetailBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private val firestoreUserRef = db.collection("users")
    private val firestoreMessageRef = db.collection("messages")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessageDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)

        (activity as? AppCompatActivity)?.supportActionBar?.title = ""
        val serviceUserId = arguments?.getString("serviceUserId")

        if (serviceUserId != null) {
            getMessageUserInfo(serviceUserId)
            checkMessageHistory(serviceUserId)
        }

        return root
    }

    private fun getMessageUserInfo(uid: String) {
        firestoreUserRef.document(uid).get().addOnSuccessListener {data ->
            displayInformation(data["userName"].toString())

        }.addOnFailureListener {
            // Hide the ProgressBar in case of failure
//            binding.loadingProgressBar.visibility = View.GONE
        }
    }

    private fun displayInformation(actionBarTitle: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = actionBarTitle
    }

    private fun checkMessageHistory(uid: String){
        val sharedPreferences = activity?.getSharedPreferences("User", Context.MODE_PRIVATE)
        val myUid = (sharedPreferences?.getString("uid", null) ?: "")
        firestoreMessageRef.whereArrayContains("users", uid).get().addOnSuccessListener { documents ->
            for (document in documents) {
                // This document's 'arrayField' contains 'desiredValue'
                // Handle each document
            }
        }
            .addOnFailureListener { exception ->
                // Handle any errors
            }
    }

}