package com.example.taskterriers.ui.requests

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.taskterriers.R
import com.example.taskterriers.databinding.FragmentAddServiceBinding
import com.example.taskterriers.databinding.FragmentRequestAddBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RequestAddFragment : Fragment() {
    private var _binding: FragmentRequestAddBinding? = null
    private val binding get() = _binding!!
    private val firestore = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Add New Request"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRequestAddBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.requestNameEditText.addTextChangedListener(requestInputTextWatcher)
        binding.requestDescriptionTextEdit.addTextChangedListener(requestInputTextWatcher)

        binding.clearRequestNameButton.setOnClickListener{
            binding.requestNameEditText.text.clear()
        }
        binding.clearRequestDescriptionButton.setOnClickListener{
            binding.requestDescriptionTextEdit.text.clear()
        }

        binding.addRequestButton.setOnClickListener {
            addToFirestore()
            Log.d("requestsFirestore", "Clicked")
        }

        return root
    }

    private fun addToFirestore(){
        val sharedPreferences = activity?.getSharedPreferences("User", Context.MODE_PRIVATE)
        val newRequestInfo = hashMapOf(
            "requestName" to binding.requestNameEditText.text.toString(),
            "serviceDescription" to binding.requestDescriptionTextEdit.text.toString(),
            "requestType" to requestType(),
            "userName" to (sharedPreferences?.getString("username", null) ?: ""),
            "uid" to (sharedPreferences?.getString("uid", null) ?: ""),
            "createdAt" to Timestamp.now(),
        )
        firestore.collection("requests").document().set(newRequestInfo).addOnSuccessListener {
            clearAllInputs()
            Toast.makeText(
                activity,
                R.string.service_added_toast,
                Toast.LENGTH_SHORT
            ).show()
        }.addOnFailureListener {
            Log.e("FirestoreError", "Error adding document", it)
            Toast.makeText(
                activity,
                R.string.service_add_fail_toast,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun requestType(): String {
        var selectedRadioId: Int = binding.requestTypeRadioGroup.checkedRadioButtonId
        val selectedRadioButton: RadioButton = requireView().findViewById(selectedRadioId)
        Log.d("clicked radio", selectedRadioButton.text.toString())

        return selectedRadioButton.text.toString()
    }

    private val requestInputTextWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val requestName = binding.requestNameEditText.text.toString().trim()
            val requestDescription = binding.requestDescriptionTextEdit.text.toString().trim()

            binding.addRequestButton.isEnabled = requestName.isNotEmpty() && requestDescription.isNotEmpty()
        }

        override fun afterTextChanged(s: Editable?) {}

    }

    private fun clearAllInputs(){
        binding.requestNameEditText.text.clear()
        binding.requestDescriptionTextEdit.text.clear()
        binding.requestTypeEducation.isChecked = true
    }


}