package com.example.taskterriers.ui.services

import android.content.Context
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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.taskterriers.R
import com.example.taskterriers.databinding.FragmentAddServiceBinding
import com.example.taskterriers.databinding.FragmentServicesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddServiceFragment : Fragment() {
    private var _binding: FragmentAddServiceBinding? = null
    private val binding get() = _binding!!
    val firestore = Firebase.firestore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Add New Service"
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
            "userName" to (sharedPreferences?.getString("username", null) ?: ""),
            "uid" to (sharedPreferences?.getString("uid", null) ?: ""),
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
}