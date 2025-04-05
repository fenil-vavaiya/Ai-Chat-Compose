package com.example.ai_chat_compose.data.repo

import com.example.ai_chat_compose.data.dao.ChatMessageDao
import com.example.ai_chat_compose.data.dao.ConversationDao
import com.example.ai_chat_compose.data.model.ChatMessage
import com.example.ai_chat_compose.data.model.Conversation
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val conversationDao: ConversationDao,
    private val messageDao: ChatMessageDao
) {

    fun getConversations(userId: String) = conversationDao.getConversationsByUser(userId)

    fun getMessages(conversationId: String) = messageDao.getMessages(conversationId)

    suspend fun createConversation(userId: String, title: String): Conversation {
        val conversation = Conversation(userId = userId, title = title)
        conversationDao.insertConversation(conversation)
        return conversation
    }

    suspend fun sendMessage(conversationId: String, userId: String, message: String, isFromUser: Boolean) {
        val chatMessage = ChatMessage(
            conversationId = conversationId,
            userId = userId,
            message = message,
            isFromUser = isFromUser
        )
        messageDao.insertMessage(chatMessage)
    }
      suspend fun updateConversationTitle(conversationId: String, newTitle: String) {
        conversationDao.updateTitle(conversationId, newTitle)
    }


    suspend fun deleteConversation(conversation: Conversation) {
        conversationDao.deleteConversation(conversation)
    }

    suspend fun updateChatMessage(chatMessage: ChatMessage) {
        messageDao.updateMessage(chatMessage)
    }

      suspend fun deleteMessageById(messageId: Int) {
        messageDao.deleteMessageById(messageId)
    }


}
