package com.example.ai_chat_compose.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai_chat_compose.data.model.ChatMessage
import com.example.ai_chat_compose.data.repo.ChatRepository
import com.example.ai_chat_compose.util.utility.getUserIdFromGoogleSignIn
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val repository: ChatRepository
) : ViewModel() {


    private val _currentUserId = MutableStateFlow(getUserIdFromGoogleSignIn()) // 🔐
    private val _selectedConversationId = MutableStateFlow<String?>(null)

    val conversations = _currentUserId.flatMapLatest {
        repository.getConversations(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val messages = _selectedConversationId.filterNotNull().flatMapLatest {
        repository.getMessages(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun createNewConversation(title: String = "New Chat") {
        viewModelScope.launch {
            val conversation = repository.createConversation(_currentUserId.value, title)
            _selectedConversationId.value = conversation.id
        }
    }

    fun selectConversation(conversationId: String) {
        _selectedConversationId.value = conversationId
    }

    fun updateChatMessage(chatMessage: ChatMessage) {
        viewModelScope.launch {
            repository.updateChatMessage(chatMessage)
        }
    }

    fun sendMessage(question: String) {
        val convId = _selectedConversationId.value ?: return

        viewModelScope.launch {
            try {
                // ✅ Step 0: Check if this is the first user message
                val existingMessages = repository.getMessages(convId).first()
                val isFirstUserMessage = existingMessages.none { it.isFromUser }

                // ✅ Step 1: Add user message to DB
                repository.sendMessage(convId, _currentUserId.value, question, isFromUser = true)

                // ✅ Step 2: If it's the first message, use it as the conversation title
                if (isFirstUserMessage) {
                    repository.updateConversationTitle(convId, question)
                }

                // ✅ Step 3: Add "Typing..." temp message
                repository.sendMessage(
                    convId,
                    _currentUserId.value,
                    "Typing...",
                    isFromUser = false
                )

                // Step 3: Load chat history for Gemini
                val history = repository.getMessages(convId)
                    .first() // Get current list once
                    .map {
                        content(if (it.isFromUser) "user" else "model") { text(it.message) }
                    }


                val chat = generativeModel.startChat(history)
                val response = chat.sendMessage(question)

                // ✅ Step 5: Remove "Typing..." and add real response
                val updatedMessages = repository.getMessages(convId).first()
                val typingMessage = updatedMessages.lastOrNull()
                if (typingMessage?.message == "Typing...") {
                    typingMessage.id?.let { repository.deleteMessageById(it) }
                }

                repository.sendMessage(
                    convId,
                    _currentUserId.value,
                    response.text.toString(),
                    isFromUser = false
                )

            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error sending message: ${e.message}")
                repository.sendMessage(
                    convId,
                    _currentUserId.value,
                    "Error: ${e.message}",
                    isFromUser = false
                )
            }
        }
    }


}
