package com.example.taskterriers.ui.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationRequest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.taskterriers.R
import com.example.taskterriers.databinding.FragmentAddServiceBinding
import com.example.taskterriers.databinding.FragmentServicesBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.time.LocalDate

open class AddServiceFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentAddServiceBinding? = null
    private val binding get() = _binding!!
    private val firestore = Firebase.firestore

    private var mMap: GoogleMap? = null
    private var location: LatLng = LatLng(42.3509, 71.1089)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Add New Service"

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddServiceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.serviceNameTextEdit.addTextChangedListener(serviceInputTextWatcher)
        binding.serviceDescriptionTextEdit.addTextChangedListener(serviceInputTextWatcher)
        binding.serviceRateTextEdit.addTextChangedListener(serviceInputTextWatcher)

        binding.addServiceButton.setOnClickListener {
            addToFirestore()
//            val fm : FragmentManager = requireActivity().supportFragmentManager
//            fm.popBackStack()
        }

        binding.locationSearchButton.setOnClickListener {
            location = searchLocation(it)
        }
        return root
    }

    private fun addToFirestore(){
        val sharedPreferences = activity?.getSharedPreferences("User", Context.MODE_PRIVATE)

        val newServiceInfo = hashMapOf(
            "serviceName" to binding.serviceNameTextEdit.text.toString(),
            "serviceDescription" to binding.serviceDescriptionTextEdit.text.toString(),
            "displayMajor" to binding.displayOptionCheckBox.isChecked,
            "serviceRate" to binding.serviceRateTextEdit.text.toString().toInt(),
            "serviceType" to serviceType(),
            "latitude" to location.latitude.toString(),
            "longitude" to location.longitude.toString(),
            "userName" to (sharedPreferences?.getString("username", null) ?: ""),
            "uid" to (sharedPreferences?.getString("uid", null) ?: ""),
            "createdAt" to Timestamp.now(),
        )
        firestore.collection("services").document().set(newServiceInfo).addOnSuccessListener {
            clearAllInputs()
            Toast.makeText(
                    activity,
                    R.string.service_added_toast,
                    Toast.LENGTH_SHORT
                ).show()
        }.addOnFailureListener {
                Toast.makeText(
                    activity,
                    R.string.service_add_fail_toast,
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    private fun clearAllInputs(){
        binding.serviceNameTextEdit.text.clear()
        binding.serviceDescriptionTextEdit.text.clear()
        binding.serviceRateTextEdit.text.clear()
        binding.displayOptionCheckBox.isChecked = false
        binding.serviceTypeEducation.isChecked = true
        binding.locationSearchEditText.text.clear()
    }

    private fun serviceType(): String {
        var selectedRadioId: Int = binding.serviceTypeRadioGroup.checkedRadioButtonId
        val selectedRadioButton: RadioButton = requireView().findViewById(selectedRadioId)
        Log.d("clicked radio", selectedRadioButton.text.toString())

        return selectedRadioButton.text.toString()
    }

    private val serviceInputTextWatcher = object: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val serviceName = binding.serviceNameTextEdit.text.toString().trim()
            val serviceDescription = binding.serviceDescriptionTextEdit.text.toString().trim()
            val serviceRate = binding.serviceRateTextEdit.text.toString().trim()

            binding.addServiceButton.isEnabled = serviceName.isNotEmpty() && serviceDescription.isNotEmpty() && serviceRate.isNotEmpty()
        }

        override fun afterTextChanged(s: Editable?) {}

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap;
    }

    fun searchLocation(view: View): LatLng {
        val location = binding.locationSearchEditText.text.toString().trim()
        if (location.isEmpty()) {
            Toast.makeText(requireContext(), "Please provide a location", Toast.LENGTH_SHORT).show()
            return LatLng(42.3509, 71.1089)
        }

        val geocoder = Geocoder(requireContext())
        val addressList: List<Address>?

        try {
            addressList = geocoder.getFromLocationName(location, 1)
            if (addressList.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
                return LatLng(42.3509, 71.1089)
            }

            val address = addressList[0]
            val latLng = LatLng(address.latitude, address.longitude)

            updateMapLocation(latLng, location)
            return latLng
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error finding location", Toast.LENGTH_SHORT).show()
        }

        return LatLng(42.3509, 71.1089)
    }

    private fun updateMapLocation(latLng: LatLng, locationName: String) {
        mMap?.apply {
            clear() // Remove existing markers
            addMarker(MarkerOptions().position(latLng).title(locationName))
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }

}