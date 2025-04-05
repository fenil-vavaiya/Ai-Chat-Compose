package com.example.ai_chat_compose.ui.screen.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ai_chat_compose.R
import com.example.ai_chat_compose.data.model.ChatMessage
import com.example.ai_chat_compose.presentation.viewmodel.ChatViewModel
import com.example.ai_chat_compose.ui.theme.ColorModelMessage
import com.example.ai_chat_compose.ui.theme.ColorModelMessageText
import com.example.ai_chat_compose.ui.theme.ColorUserMessage
import com.example.ai_chat_compose.ui.theme.Grey
import com.example.ai_chat_compose.ui.theme.Grey2
import com.example.ai_chat_compose.ui.theme.Nunito
import com.example.ai_chat_compose.ui.theme.Theme
import com.example.ai_chat_compose.util.Const.TAG
import com.example.ai_chat_compose.util.utility.SetStatusBarColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(chatViewModel: ChatViewModel = hiltViewModel()) {

    SetStatusBarColor(darkIcons = true)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            DrawerContent(

                chatViewModel = chatViewModel,
                onItemClick = {
                    scope.launch { drawerState.close() }
                },
                onNewConvoClick = {}
            )
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(onHistoryClick = { scope.launch { drawerState.open() } }, onNewsClick = {})

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                MessageList(
                    modifier = Modifier.fillMaxSize(), chatViewModel = chatViewModel
                )
            }

            MessageInput(
                onMessageSend = {
                    Log.d(TAG, "HomeScreen: MessageInput = $it")
                    chatViewModel.sendMessage(it)
                },
                modifier = Modifier.navigationBarsPadding() // Ensures proper padding at the bottom
            )
        }
    }
}


@Composable
private fun Header(onHistoryClick: () -> Unit, onNewsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 30.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        IconButton(onClick = { onHistoryClick() }) {
            Icon(
                painterResource(R.drawable.ic_history),
                contentDescription = "History",
                tint = Theme,
                modifier = Modifier.size(30.dp)
            )
        }

        Text(
            text = "Talk to me", style = TextStyle(
                fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 20.sp
            ), modifier = Modifier.weight(1f)
        )

        IconButton(onClick = { onNewsClick() }) {
            Icon(
                painterResource(R.drawable.ic_news), tint = Theme, contentDescription = "News",
                modifier = Modifier.size(30.dp)
            )
        }
    }

    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(horizontal = 10.dp), color = Grey
    )
}

@Composable
fun MessageList(modifier: Modifier = Modifier, chatViewModel: ChatViewModel) {

    val messageList by chatViewModel.messages.collectAsState()

    if (messageList.isEmpty()) {
        chatViewModel.createNewConversation()
        SuggestionsInput(modifier, chatViewModel)
    } else {
        LazyColumn(
            modifier = modifier, reverseLayout = true
        ) {
            items(messageList.reversed()) { message ->
                MessageItem(messageModel = message, chatViewModel = chatViewModel)
            }
        }
    }
}

@Composable

private fun SuggestionsInput(
    modifier: Modifier = Modifier, chatViewModel: ChatViewModel = hiltViewModel()
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {

            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_explain),
                        contentDescription = "Icon",
                    )

                    SuggestionText("Explain Quantum physics") { chatViewModel.sendMessage(it) }
                    SuggestionText("What are wormholes explain like i am 5") {
                        chatViewModel.sendMessage(
                            it
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Icon",
                    )

                    SuggestionText("Write a tweet about global warming") {
                        chatViewModel.sendMessage(
                            it
                        )
                    }
                    SuggestionText("Write a poem about flower and love") {
                        chatViewModel.sendMessage(
                            it
                        )
                    }
                    SuggestionText("Write a rap song lyrics about") {
                        chatViewModel.sendMessage(
                            it
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_translate),
                        contentDescription = "Icon",
                    )

                    SuggestionText("Write a tweet about global warming") {
                        chatViewModel.sendMessage(
                            it
                        )
                    }
                    SuggestionText("Write a poem about flower and love") {
                        chatViewModel.sendMessage(
                            it
                        )
                    }
                    SuggestionText("Write a rap song lyrics about") {
                        chatViewModel.sendMessage(
                            it
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun SuggestionText(text: String, OnClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(color = Grey2)
            .padding(vertical = 15.dp)
            .clickable { OnClick(text) }) {
        Text(
            text = text, style = TextStyle(
                fontFamily = Nunito,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Center // Centers text inside the Text composable
            ), modifier = Modifier.fillMaxWidth()
        )

    }
}

@Composable
fun MessageItem(messageModel: ChatMessage, chatViewModel: ChatViewModel) {
    val isModel = !messageModel.isFromUser
    val bubbleShape = if (isModel) {
        RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
    }

    val horizontalAlignment = if (isModel) Alignment.Start else Alignment.End

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = Modifier
            .padding(horizontal = 18.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        BoxWithConstraints {
            Card(
                colors = CardDefaults.cardColors(containerColor = if (isModel) ColorModelMessage else ColorUserMessage),
                shape = bubbleShape,
                modifier = Modifier
                    .widthIn(0.dp, maxWidth * 0.9f)
                    .padding(vertical = 5.dp)
            ) {
                SelectionContainer {
                    TypingTextAnimation(
                        fullText = messageModel.message,
                        isModel = isModel,
                        shouldAnimate = !messageModel.hasAnimated, // Animate only if not animated before
                        messageModel = messageModel,
                        chatViewModel = chatViewModel
                    )
                }
            }
        }
    }
}


@Composable
fun TypingTextAnimation(
    fullText: String,
    isModel: Boolean,
    shouldAnimate: Boolean,
    messageModel: ChatMessage,
    chatViewModel: ChatViewModel,
    typingSpeed: Long = 10L
) {
    var displayedText by remember(fullText) {
        mutableStateOf(if (!isModel || !shouldAnimate) fullText else "")
    }

    LaunchedEffect(fullText) {
        if (isModel && shouldAnimate) { // Animate only for model messages
            displayedText = ""
            messageModel.hasAnimated = true
            chatViewModel.updateChatMessage(messageModel)
            fullText.forEachIndexed { index, _ ->
                displayedText = fullText.take(index + 1)
                delay(typingSpeed)
            }

        }
    }

    Text(
        text = displayedText.trim(), style = TextStyle(
            fontFamily = Nunito,
            fontWeight = FontWeight.Bold,
            color = if (isModel) ColorModelMessageText else Color.White,
            fontSize = 14.sp
        ), modifier = Modifier.padding(16.dp)
    )
}


/*@Composable
fun ChatTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        textStyle = TextStyle(
            fontFamily = Nunito,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = R.color.black),
            fontSize = 18.sp
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp) // ✅ Fixed height without compression
            .background(Color.Transparent)
            .padding(horizontal = 10.dp, vertical = 8.dp), // ✅ Fine-tune text positioning
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.messageWithDot),
                        color = etHint,
                        fontSize = 18.sp
                    )
                }
                innerTextField()
            }
        }
    )
}*/






