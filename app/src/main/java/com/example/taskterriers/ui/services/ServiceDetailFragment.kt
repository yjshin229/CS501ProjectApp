package com.example.taskterriers.ui.services

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.taskterriers.databinding.FragmentServiceDetailBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
class ServiceDetailFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentServiceDetailBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private val firestoreRef = db.collection("services")
    private lateinit var googleMap: GoogleMap

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

        val serviceId = arguments?.getString("serviceId")
        if (serviceId != null) {
            firestoreRef.document(serviceId).get().addOnSuccessListener {data ->
                binding.userNameTextView.text = data["userName"].toString()
                binding.serviceDescriptionTextView.text = data["serviceDescription"].toString()
                (activity as? AppCompatActivity)?.supportActionBar?.title = data["serviceName"].toString()
            }
        }

        return root
    }


    override fun onMapReady(map: GoogleMap) {
        map?.let{
            googleMap = it
        }
    }
}