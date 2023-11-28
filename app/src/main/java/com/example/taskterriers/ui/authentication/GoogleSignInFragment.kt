package com.example.taskterriers.ui.authentication

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.taskterriers.R
import com.example.taskterriers.databinding.FragmentGoogleSignInBinding
import com.example.taskterriers.databinding.FragmentMessagesBinding
import com.example.taskterriers.ui.messages.MessagesViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.play.integrity.internal.v
import com.google.firebase.auth.FirebaseAuth

class GoogleSignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private var _binding: FragmentGoogleSignInBinding? = null

    private val binding get() = _binding!!

    companion object {
        fun newInstance() = GoogleSignInFragment()
    }

    private lateinit var viewModel: GoogleSignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val GoogleSignInViewModel =
            ViewModelProvider(this)[GoogleSignInViewModel::class.java]

        _binding = FragmentGoogleSignInBinding.inflate(inflater, container, false)
        val root: View = binding.root



//        binding.googleSignInButton.setOnClickListener(v -> {
//            Navigation.findNavController(v)
//        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}