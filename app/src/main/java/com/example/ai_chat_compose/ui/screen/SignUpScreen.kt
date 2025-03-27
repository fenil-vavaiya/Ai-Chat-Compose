package com.example.ai_chat_compose.ui.screen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ai_chat_compose.R
import com.example.ai_chat_compose.data.model.UserModel
import com.example.ai_chat_compose.domain.Resource
import com.example.ai_chat_compose.presentation.viewmodel.AuthViewModel
import com.example.ai_chat_compose.presentation.viewmodel.MainViewModel
import com.example.ai_chat_compose.ui.theme.Nunito
import com.example.ai_chat_compose.util.Const
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: MainViewModel,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val oneTapClient = Identity.getSignInClient(context)
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    authViewModel.signInWithGoogle(idToken)
                }
            }
        }

    val signInState by authViewModel.signInResult.collectAsState()

    Box() {
        Image(
            painter = painterResource(R.drawable.img_signup_bg),
            contentDescription = "Signup",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Maintains aspect ratio but fills the parent
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Welcome to AI-Chat", style = TextStyle(
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.White
                )
            )
            Text(
                text = "Join over 10,000 learners worldwide and\n enjoy online education!",
                style = TextStyle(
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            when (signInState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(color = Color.White)
                }

                is Resource.Success -> {
                    Text(
                        text = "Sign-in successful! Welcome ${(signInState as Resource.Success<FirebaseUser>).data.displayName}",
                        color = Color.Green,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    // Navigate to next screen after success
                    LaunchedEffect(Unit) {
                        scope.launch {
                            val user = (signInState as Resource.Success<FirebaseUser>).data
                            viewModel.dataStoreManager.saveObject(
                                Const.USER_MODEL, UserModel(
                                    user.displayName ?: "Demo User",
                                    user.email ?: "demoEmail",
                                    user.photoUrl.toString()
                                )
                            )
                            viewModel.saveLoginStatus(true)
                            navController.navigate("onboarding") {
                                popUpTo("signup") { inclusive = true }
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    Text(
                        text = "Error: ${(signInState as Resource.Error).message}",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                else -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = Color.White)
                            .clickable {
                                scope.launch {
                                    authViewModel.getSignInIntent()?.let { intentSender ->
                                        launcher.launch(
                                            IntentSenderRequest.Builder(intentSender).build()
                                        )
                                    }
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Spacer(modifier = Modifier.weight(1f)) // Pushes text to center
                        Text(
                            text = "Sign in with Google", style = TextStyle(
                                fontFamily = Nunito,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ), modifier = Modifier.weight(2f), textAlign = TextAlign.Center
                        )
                        Image(
                            painter = painterResource(R.drawable.ic_google),
                            contentDescription = "Google Icon",
                            modifier = Modifier
                                .size(25.dp)
                                .weight(1f)
                        )
                    }
                }
            }
        }
    }
}


/*scope.launch {
                            viewModel.saveLoginStatus(true)
                            navController.navigate("onboarding") {
                                popUpTo("signup") { inclusive = true }
                            }
                        }*/