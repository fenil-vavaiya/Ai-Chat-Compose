package com.example.ai_chat_compose.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai_chat_compose.data.model.MessageModel
import com.example.ai_chat_compose.util.Const.TAG
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val generativeModel: GenerativeModel // Injected via Hilt
) : ViewModel() {

    private val _messageList = MutableStateFlow<List<MessageModel>>(emptyList()) // StateFlow for live updates
    val messageList: StateFlow<List<MessageModel>> = _messageList.asStateFlow() // Exposed as immutable Flow

    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat(
                    history = _messageList.value.map {
                        content(it.role) { text(it.message) }
                    }
                )

                // Update list reactively
                _messageList.update { it + MessageModel(question, "user") + MessageModel("Typing....", "model") }

                val response = chat.sendMessage(question)

                _messageList.update { currentList ->
                    currentList.dropLast(1) + MessageModel(response.text.toString(), "model") // Remove "Typing..." and add response
                }
            } catch (e: Exception) {

                Log.d(TAG, "sendMessage: message = "+e.message)
                _messageList.update { currentList ->
                    currentList.dropLast(1) + MessageModel("Error: ${e.message}", "model")
                }
            }
        }
    }
}
