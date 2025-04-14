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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val conversations = _currentUserId.flatMapLatest {
        repository.getConversations(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val messages = _selectedConversationId
        .flatMapLatest { convId ->
            if (convId == null) flowOf(emptyList())
            else repository.getMessages(convId)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    fun startNewConversation() {
        _selectedConversationId.value = null
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
        viewModelScope.launch {
            var convId = _selectedConversationId.value
            try {
                if (convId == null) {
                    val conversation = repository.createConversation(
                        userId = _currentUserId.value,
                        title = question // Use first user question as title
                    )
                    convId = conversation.id
                    _selectedConversationId.value = convId
                }
                repository.sendMessage(convId, _currentUserId.value, question, isFromUser = true)
                repository.sendMessage(
                    convId,
                    _currentUserId.value,
                    "Typing...",
                    isFromUser = false
                )
                val history = repository.getMessages(convId)
                    .first()
                    .map {
                        content(if (it.isFromUser) "user" else "model") { text(it.message) }
                    }
                val chat = generativeModel.startChat(history)
                val response = chat.sendMessage(question)
                val currentMessages = repository.getMessages(convId).first()
                val typingMessage = currentMessages.lastOrNull()
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
                if (convId != null) {
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

}
