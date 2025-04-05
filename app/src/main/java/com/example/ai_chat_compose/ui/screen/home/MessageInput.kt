package com.example.ai_chat_compose.ui.screen.home

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ai_chat_compose.R
import com.example.ai_chat_compose.ui.theme.Nunito
import com.example.ai_chat_compose.ui.theme.Theme
import com.example.ai_chat_compose.ui.theme.etHint
import com.example.ai_chat_compose.util.Const.TAG

@Composable
fun MessageInput(onMessageSend: (String) -> Unit, modifier: Modifier) {

    var message by remember { mutableStateOf("") }

    val context = LocalContext.current

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d(TAG, "Speech Recognizer Result: ${result.resultCode}")
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val speechText =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.getOrNull(0)
                    ?: "No speech detected"
            message = speechText
            Log.d(TAG, "Recognized Speech: $message")
        } else {
            Log.e(TAG, "Speech Recognition Failed or Cancelled")
        }
    }


    fun startSpeechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra("calling_package", javaClass.getPackage()?.name)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en")
        }
        Log.d(TAG, "Launching Speech Recognizer")
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
                if (ContextCompat.checkSelfPermission(
                        context, Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
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