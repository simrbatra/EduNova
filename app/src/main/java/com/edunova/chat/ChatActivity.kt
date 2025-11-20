package com.edunova.chat

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edunova.R
import com.edunova.databinding.ActivityChatBinding
import com.edunova.databinding.ItemChatMessageBinding
import com.edunova.data.api.EducationApiService
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

data class ChatMessage(
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val apiService = EducationApiService()
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var chatAdapter: ChatAdapter
    private var isSending = false

    companion object {
        private const val TAG = "ChatActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupSendButton()
        setupInputField()

        // Add welcome message
        addMessage("Hello! I'm your AI tutor. How can I help you today?", false)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.toolbar.title = "AI Tutor - Ask Your Doubts"
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messages)
        binding.recyclerViewChat.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
            itemAnimator = null // Disable animations for smoother scrolling
        }
    }

    private fun setupInputField() {
        val inputEditText = binding.root.findViewById<TextInputEditText>(R.id.inputBox)
        inputEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND && !isSending) {
                binding.sendButton.performClick()
                true
            } else {
                false
            }
        }
    }

    private fun setupSendButton() {
        binding.sendButton.setOnClickListener {
            if (isSending) return@setOnClickListener
            
            val userMessage = binding.inputBox.text?.toString()?.trim()
            if (!userMessage.isNullOrEmpty()) {
                addMessage(userMessage, true)
                binding.inputBox.text?.clear()
                binding.inputBox.clearFocus()
                sendMessageToAI(userMessage)
            }
        }
    }

    private fun sendMessageToAI(userMessage: String) {
        if (isSending) return
        
        isSending = true
        binding.progressBar.visibility = View.VISIBLE
        binding.sendButton.isEnabled = false
        binding.inputBox.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.chatWithAI(
                        message = userMessage,
                        context = "You are a friendly and helpful educational AI tutor. Your role is to help students understand concepts, answer questions, and clear their doubts. Be encouraging, clear, and educational in your responses. Keep responses concise but informative."
                    )
                }
                
                withContext(Dispatchers.Main) {
                    addMessage(response, false)
                    binding.progressBar.visibility = View.GONE
                    binding.sendButton.isEnabled = true
                    binding.inputBox.isEnabled = true
                    isSending = false
                    binding.recyclerViewChat.smoothScrollToPosition(messages.size - 1)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error sending message to AI", e)
                withContext(Dispatchers.Main) {
                    val errorMessage = when {
                        e.message?.contains("GEMINI_API_KEY", ignoreCase = true) == true ||
                        e.message?.contains("API key", ignoreCase = true) == true ||
                        e.message?.contains("not configured", ignoreCase = true) == true -> 
                            "⚠️ AI Service Configuration Required\n\n" +
                            "The Gemini API key is not configured. To enable the AI chatbot:\n\n" +
                            "1. Get your API key from:\n" +
                            "   https://makersuite.google.com/app/apikey\n\n" +
                            "2. Open gradle.properties in the project root\n\n" +
                            "3. Replace YOUR_API_KEY_HERE with your actual API key\n\n" +
                            "4. Rebuild the app"
                        e.message?.contains("network", ignoreCase = true) == true -> 
                            "🌐 Network Error\n\nPlease check your internet connection and try again."
                        else -> 
                            "❌ Error\n\nSorry, I encountered an error: ${e.message ?: "Unknown error"}\n\nPlease try again."
                    }
                    addMessage(errorMessage, false)
                    binding.progressBar.visibility = View.GONE
                    binding.sendButton.isEnabled = true
                    binding.inputBox.isEnabled = true
                    isSending = false
                    // Only show toast for non-API key errors to avoid spam
                    if (e.message?.contains("API", ignoreCase = true) != true) {
                        Toast.makeText(this@ChatActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun addMessage(message: String, isUser: Boolean) {
        messages.add(ChatMessage(message, isUser))
        chatAdapter.notifyItemInserted(messages.size - 1)
        // Scroll to bottom after a short delay to ensure smooth animation
        binding.recyclerViewChat.post {
            if (messages.isNotEmpty()) {
                binding.recyclerViewChat.smoothScrollToPosition(messages.size - 1)
            }
        }
    }
}

class ChatAdapter(private val messages: List<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatMessageBinding.inflate(
            android.view.LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.size

    class ChatViewHolder(private val binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessage) {
            binding.textMessage.text = message.message
            
            // Set card background and text color based on user/AI
            val cardView = binding.root.getChildAt(0) as? com.google.android.material.card.MaterialCardView
            if (cardView != null) {
                if (message.isUser) {
                    cardView.setCardBackgroundColor(
                        binding.root.context.getColor(com.edunova.R.color.primary)
                    )
                    binding.textMessage.setTextColor(
                        binding.root.context.getColor(com.edunova.R.color.text_on_primary)
                    )
                    (binding.root as android.widget.LinearLayout).gravity = android.view.Gravity.END
                } else {
                    cardView.setCardBackgroundColor(
                        binding.root.context.getColor(com.edunova.R.color.surface_variant)
                    )
                    binding.textMessage.setTextColor(
                        binding.root.context.getColor(com.edunova.R.color.text_primary)
                    )
                    (binding.root as android.widget.LinearLayout).gravity = android.view.Gravity.START
                }
            }
        }
    }
}
