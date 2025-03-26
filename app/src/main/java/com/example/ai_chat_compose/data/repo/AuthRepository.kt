package com.example.ai_chat_compose.data.repo

import android.content.IntentSender
import com.example.ai_chat_compose.domain.Resource
import com.example.ai_chat_compose.util.Const
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val oneTapClient: SignInClient
) {
    suspend fun signInWithGoogle(idToken: String): Resource<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            Resource.Success(authResult.user!!)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Authentication Failed")
        }
    }

      suspend fun getSignInIntent(): IntentSender? {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(Const.SERVER_CLIENT_ID) // Replace with your Web Client ID
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        return try {
            oneTapClient.beginSignIn(signInRequest).await().pendingIntent.intentSender
        } catch (e: Exception) {
            null
        }
    }
}

