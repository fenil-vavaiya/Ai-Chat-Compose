package com.example.ai_chat_compose.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_chat_compose.R
import com.example.ai_chat_compose.presentation.viewmodel.ChatViewModel
import com.example.ai_chat_compose.ui.theme.Grey2
import com.example.ai_chat_compose.ui.theme.Nunito

@Composable
fun DrawerContent(
    chatViewModel: ChatViewModel, onNewConvoClick: () -> Unit, onItemClick: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val drawerWidth = screenWidth * 0.85f
    val conversationList by chatViewModel.conversations.collectAsState()
    val listState = rememberLazyListState()

    var searchQuery by remember { mutableStateOf("") }

    val filteredConversations = conversationList.filter {
        it.title.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .width(drawerWidth)
            .fillMaxHeight()
            .background(Color.White)
            .padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DrawerHeader(onTextChange = {searchQuery = it}, onNewConvoClick =  onNewConvoClick)

        Text(
            text = "Conversations",
            style = TextStyle(
                fontFamily = Nunito,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
        )

        LazyColumn(state = listState) {
            items(filteredConversations.size) {
                val conversation = filteredConversations[it]
                Text(
                    text = conversation.title,
                    style = TextStyle(
                        fontFamily = Nunito,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 15.dp)
                        .clickable { onItemClick(); chatViewModel.selectConversation(conversation.id) }
                )
            }
        }

    }
}

@Composable
private fun DrawerHeader(onTextChange: (String) -> Unit, onNewConvoClick: () -> Unit) {
    var searchInput by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(CircleShape)
                .background(color = Grey2)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painterResource(R.drawable.ic_search),
                contentDescription = "search",
                modifier = Modifier
                    .size(20.dp)
                    .padding()
            )
            ChatTextField(
                text = searchInput,
                onTextChange = { searchInput = it; onTextChange(searchInput) },
                modifier = Modifier.weight(1f)
            )
        }
        IconButton({ onNewConvoClick() }) {
            Icon(
                painterResource(R.drawable.ic_new_chat),
                contentDescription = "new conversation",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}