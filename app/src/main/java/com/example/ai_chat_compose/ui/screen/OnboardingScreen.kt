package com.example.ai_chat_compose.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavController
import com.example.ai_chat_compose.R
import com.example.ai_chat_compose.presentation.viewmodel.MainViewModel
import com.example.ai_chat_compose.ui.theme.Grey
import com.example.ai_chat_compose.ui.theme.Nunito
import com.example.ai_chat_compose.ui.theme.Theme
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(navController: NavController, viewModel: MainViewModel) {
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.padding(bottom = 50.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 60.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "You AI Assistant",
                    style = TextStyle(
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = Theme
                    ),

                    )
                Text(
                    text = "Using this software,you can ask you\n" + "questions and receive articles using\n" + "artificial intelligence assistant",
                    style = TextStyle(
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Grey
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Image(
                painterResource(R.drawable.img_intro_1),
                contentDescription = "Intro",
                modifier = Modifier
                    .size(300.dp)
                    .weight(2f)
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .weight(0.2f)
                    .padding(horizontal = 20.dp)
                    .offset(y = 10.dp) // Moves the Row down
                    .clip(RoundedCornerShape(40.dp))
                    .background(color = Theme)
                    .clickable {
                        scope.launch {
                            viewModel.saveOnboardingStatus(true)
                            navController.navigate("home") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.weight(1f)) // Pushes text to center
                Text(
                    text = "Continue", style = TextStyle(
                        fontFamily = Nunito,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ), modifier = Modifier.weight(2f), // Centers the text
                    textAlign = TextAlign.Center
                )

                Image(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = "Arrow",
                    modifier = Modifier
                        .size(25.dp)
                        .weight(1f) // Pushes the image to the end
                )
            }
        }
    }
}
