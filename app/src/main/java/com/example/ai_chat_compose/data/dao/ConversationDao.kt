package com.example.ai_chat_compose.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.ai_chat_compose.data.model.Conversation
import com.example.ai_chat_compose.data.model.ConversationWithMessages
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {

    @Query("SELECT * FROM conversations WHERE userId = :userId ORDER BY createdAt DESC")
    fun getConversationsByUser(userId: String): Flow<List<Conversation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation)

    @Delete
    suspend fun deleteConversation(conversation: Conversation)

    @Transaction
    @Query("SELECT * FROM conversations WHERE id = :conversationId")
    fun getConversationWithMessages(conversationId: String): Flow<ConversationWithMessages>

    @Query("UPDATE conversations SET title = :newTitle WHERE id = :conversationId")
    suspend fun updateTitle(conversationId: String, newTitle: String)

}
