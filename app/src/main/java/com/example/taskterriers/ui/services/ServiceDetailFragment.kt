package com.example.taskterriers.ui.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.taskterriers.R
import com.example.taskterriers.databinding.FragmentServiceDetailBinding
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
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
    private var serviceUserId: String = ""
    private lateinit var serviceUserName: String

    private var mMap: GoogleMap? = null
    private var location: LatLng = LatLng(42.3509, 71.1089)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?  // Use safe cast (as?)
        mapFragment?.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentServiceDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        beforeLoadInformation()
        val sharedPreferences = activity?.getSharedPreferences("User", AppCompatActivity.MODE_PRIVATE)
        val uid = (sharedPreferences?.getString("uid", null) ?: "")

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
                binding.loadingProgressBar.visibility = View.GONE
                serviceUserId = data["uid"].toString()
                serviceUserName = data["userName"].toString()
                location = LatLng(data["latitude"].toString().toDouble() , data["longitude"].toString().toDouble())
                updateMapLocation(location)
                if(serviceUserId != uid){
                    binding.sendMessageButton.visibility = View.VISIBLE
                }
                displayInformation(data["serviceName"].toString())

            }.addOnFailureListener {
                // Hide the ProgressBar in case of failure
                binding.loadingProgressBar.visibility = View.GONE
            }

        }

        binding.sendMessageButton.setOnClickListener {
            val actionId = R.id.action_serviceDetailFragment_to_messageDetailFragment
            val bundle = Bundle().apply {
                putString("chatUserId", serviceUserId)
                putString("chatUserName", serviceUserName)
            }
            findNavController().navigate(actionId, bundle)
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap!!.addMarker(MarkerOptions().position(location))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    private fun updateMapLocation(latLng: LatLng) {
        mMap?.apply {
            clear() // Remove existing markers
            addMarker(MarkerOptions().position(latLng))
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }

}