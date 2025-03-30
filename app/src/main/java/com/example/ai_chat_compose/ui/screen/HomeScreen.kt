package com.example.ai_chat_compose.ui.screen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ai_chat_compose.R
import com.example.ai_chat_compose.data.model.MessageModel
import com.example.ai_chat_compose.presentation.viewmodel.ChatViewModel
import com.example.ai_chat_compose.ui.theme.ColorModelMessage
import com.example.ai_chat_compose.ui.theme.ColorModelMessageText
import com.example.ai_chat_compose.ui.theme.ColorUserMessage
import com.example.ai_chat_compose.ui.theme.Grey
import com.example.ai_chat_compose.ui.theme.Grey2
import com.example.ai_chat_compose.ui.theme.Nunito
import com.example.ai_chat_compose.ui.theme.Theme
import com.example.ai_chat_compose.ui.theme.etHint
import com.example.ai_chat_compose.util.Const.TAG
import com.example.ai_chat_compose.util.utility.SetStatusBarColor
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun HomeScreen(chatViewModel: ChatViewModel = hiltViewModel()) {

    SetStatusBarColor(darkIcons = true)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header()

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
            }, modifier = Modifier.navigationBarsPadding() // Ensures proper padding at the bottom
        )
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 30.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Talk to me", style = TextStyle(
                fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 20.sp
            ), modifier = Modifier.weight(1f)
        )

        Icon(
            painterResource(R.drawable.ic_setting),
            contentDescription = "",
            modifier = Modifier.size(30.dp),
            tint = Color.Black
        )
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

    val messageList by chatViewModel.messageList.collectAsState()

    if (messageList.isEmpty()) {
        SuggestionsInput(modifier, chatViewModel)
    } else {
        LazyColumn(
            modifier = modifier, reverseLayout = true
        ) {
            items(messageList.reversed()) { message ->
                MessageItem(messageModel = message)
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
fun MessageItem(messageModel: MessageModel) {
    val isModel = messageModel.role == "model"
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
                        messageModel = messageModel
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
    messageModel: MessageModel,
    typingSpeed: Long = 10L
) {
    var displayedText by remember(fullText) {
        mutableStateOf(if (!isModel || !shouldAnimate) fullText else "")
    }

    LaunchedEffect(fullText) {
        if (isModel && shouldAnimate) { // Animate only for model messages
            displayedText = ""
            messageModel.hasAnimated = true
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


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MessageInput(onMessageSend: (String) -> Unit, modifier: Modifier) {

    var message by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val speechText =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull() ?: ""
            message = speechText
        }
    }

    fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
        speechLauncher.launch(intent)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startSpeechToText()
        } else {
            Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }

    Card(
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 30.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChatTextField(
                text = message, onTextChange = { message = it }, modifier = Modifier.weight(1f)
            )

            IconButton(onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    startSpeechToText()
                } else {
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }) {
                Icon(
                    painterResource(R.drawable.ic_mic),
                    contentDescription = "Mic",
                )
            }

            IconButton(onClick = {
                if (message.isNotEmpty()) {
                    onMessageSend(message)
                    message = ""
                }

            }) {
                Icon(
                    painterResource(R.drawable.ic_send), contentDescription = "Send", tint = Theme
                )
            }
        }
    }
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


@Composable
fun ChatTextField(
    text: String, onTextChange: (String) -> Unit, modifier: Modifier = Modifier
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        placeholder = { Text(stringResource(id = R.string.messageWithDot), color = etHint) },
        textStyle = TextStyle(
            fontFamily = Nunito,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = R.color.black),
            fontSize = 18.sp
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(0.dp), // ✅ Removes any additional padding
        shape = RectangleShape // ✅ Prevents extra rounded padding
    )
}



