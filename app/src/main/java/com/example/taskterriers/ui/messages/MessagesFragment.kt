package com.example.taskterriers.ui.messages

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.taskterriers.databinding.FragmentMessagesBinding

class MessagesFragment : Fragment() {

    private var _binding: FragmentMessagesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val messagesViewModel =
            ViewModelProvider(this).get(MessagesViewModel::class.java)

        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val chats =  messagesViewModel.chats
        val sharedPreferences = activity?.getSharedPreferences("User", Context.MODE_PRIVATE)
        val navController = findNavController()

        // Setting up the RecyclerView
        val adapter = ChatCardAdapter(emptyList(), navController)
        binding.recyclerViewChats.adapter = adapter

        chats.observe(viewLifecycleOwner) { chatList ->
            (binding.recyclerViewChats.adapter as ChatCardAdapter).updateData(chatList)
            binding.swipeRefreshLayout.isRefreshing = false
            binding.progressBar.visibility = View.GONE
            // Update adapter with new data
            adapter.updateData(chatList)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            messagesViewModel.refreshData()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}