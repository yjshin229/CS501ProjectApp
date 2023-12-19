package com.example.taskterriers.ui.requests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.taskterriers.R
import com.example.taskterriers.databinding.FragmentRequestAddBinding
import com.example.taskterriers.databinding.FragmentRequestDetailBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RequestDetailFragment : Fragment() {
    private var _binding: FragmentRequestDetailBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private val firestoreRef = db.collection("requests")
    private var requestUserId: String = ""
    private lateinit var requestUserName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRequestDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        beforeLoadInformation()

        val requestId = arguments?.getString("requestId")
        if (requestId != null) {
            getRequestInfo(requestId)
        }

        binding.sendMessageButton.setOnClickListener {
            val actionId = R.id.action_requestDetailFragment_to_messageDetailFragment
            val bundle = Bundle().apply {
                putString("chatUserId", requestUserId)
                putString("chatUserName",requestUserName)
            }
            findNavController().navigate(actionId, bundle)
        }

        return root
    }

    fun getRequestInfo(requestId: String) {
        val sharedPreferences = activity?.getSharedPreferences("User", AppCompatActivity.MODE_PRIVATE)
        val uid = (sharedPreferences?.getString("uid", null) ?: "")

        binding.loadingProgressBar.visibility = View.VISIBLE
        firestoreRef.document(requestId).get().addOnSuccessListener {data ->
            binding.requestNameTextView.text = data["requestName"].toString()
            requestUserName = data["userName"].toString()
            binding.userNameTextView.text = data["userName"].toString()
            binding.dateTextView.text =  getCurrentDateTimeFormatted(data.getTimestamp("createdAt")?.toDate())
            binding.requestDescriptionTextView.setText( data["requestDescription"].toString())
            binding.loadingProgressBar.visibility = View.GONE
            requestUserId = data["uid"].toString()
            if(requestUserId != uid){
                binding.sendMessageButton.visibility = View.VISIBLE
            }
            displayInformation(data["requestName"].toString())

        }.addOnFailureListener {
            // Hide the ProgressBar in case of failure
            binding.loadingProgressBar.visibility = View.GONE
        }
    }

    private fun beforeLoadInformation(){
        binding.userNameTextView.isVisible = false
        binding.requestDescriptionTextView.isVisible = false
        (activity as? AppCompatActivity)?.supportActionBar?.title = ""
    }

    private fun displayInformation(actionBarTitle: String) {
        binding.userNameTextView.isVisible = true
        binding.requestDescriptionTextView.isVisible = true
        (activity as? AppCompatActivity)?.supportActionBar?.title = actionBarTitle
    }

    private fun getCurrentDateTimeFormatted(date : Date?): String {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault())
        return dateFormat.format(date)
    }

}