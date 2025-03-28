package com.darknationchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.darknationchat.databinding.ActivityMainBinding
import com.darknationchat.service.GeminiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val chatAdapter = ChatAdapter()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var geminiService: GeminiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        geminiService = GeminiService(this)
        setupRecyclerView()
        setupSendButton()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
        }
    }

    private fun setupSendButton() {
        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString()
            if (message.isNotEmpty()) {
                sendMessage(message)
                binding.etMessage.text.clear()
            }
        }
    }

    private fun sendMessage(message: String) {
        chatAdapter.addMessage(Message(message, true))
        coroutineScope.launch {
            val response = geminiService.getResponse(message)
            chatAdapter.addMessage(response)
            binding.recyclerView.smoothScrollToPosition(chatAdapter.itemCount - 1)
        }
    }
}
