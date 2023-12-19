package com.example.taskterriers.ui.services

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.taskterriers.databinding.FragmentServiceDetailBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ServiceDetailFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentServiceDetailBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private val firestoreRef = db.collection("services")
    private lateinit var googleMap: GoogleMap
    private var serviceInfoReady = false
    private var userInfoReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        (activity as? AppCompatActivity)?.supportActionBar?.title = serviceName
//        binding.mapView.onCreate(savedInstanceState)
//        binding.mapView.onResume()
//        binding.mapView.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentServiceDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        beforeLoadInformation()

        val serviceId = arguments?.getString("serviceId")
        if (serviceId != null) {
            binding.loadingProgressBar.visibility = View.VISIBLE
            firestoreRef.document(serviceId).get().addOnSuccessListener {data ->
                binding.userNameTextView.text = data["userName"].toString()
                binding.serviceDescriptionTextView.setText( data["serviceDescription"].toString())
                binding.serviceRateTextView.text = data["serviceRate"].toString()
                if(data["displayMajor"] as Boolean){
                    CoroutineScope(Dispatchers.IO).launch {
                    val userData =  async { displayMajor(data["uid"].toString()) }
                        userData.await()
                    }
                    binding.majorLinearView.visibility = View.VISIBLE
                }else{
                    binding.majorLinearView.visibility = View.GONE
                }
                serviceInfoReady = true
                binding.loadingProgressBar.visibility = View.GONE
                displayInformation(data["serviceName"].toString())

            }.addOnFailureListener {
                // Hide the ProgressBar in case of failure
                binding.loadingProgressBar.visibility = View.GONE
            }
        }

        return root
    }

    private suspend fun displayMajor(userId: String){
        val userRef =  db.collection("users")
        userRef.document(userId).get().addOnSuccessListener {data ->
            binding.majorTextView.text = data["major"].toString()
        }.await()
    }

    private fun beforeLoadInformation(){
        binding.userNameTextView.isVisible = false
        binding.descriptionTitle.isVisible = false
        binding.serviceDescriptionTextView.isVisible = false
        binding.locationTitle.isVisible = false
        binding.dollarSign.isVisible = false
        binding.serviceRateTextView.isVisible = false
        binding.majorLinearView.isVisible = false
        (activity as? AppCompatActivity)?.supportActionBar?.title = ""
    }

    private fun displayInformation(actionBarTitle: String) {
        binding.userNameTextView.isVisible = true
        binding.descriptionTitle.isVisible = true
        binding.serviceDescriptionTextView.isVisible = true
        binding.locationTitle.isVisible = true
        binding.dollarSign.isVisible = true
        binding.serviceRateTextView.isVisible = true
        binding.majorLinearView.isVisible = true
        (activity as? AppCompatActivity)?.supportActionBar?.title = actionBarTitle
    }


    override fun onMapReady(map: GoogleMap) {
        map?.let{
            googleMap = it
        }
    }
}