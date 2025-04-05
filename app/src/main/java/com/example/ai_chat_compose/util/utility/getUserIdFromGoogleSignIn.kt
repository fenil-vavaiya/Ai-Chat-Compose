package com.example.ai_chat_compose.util.utility

import com.google.firebase.auth.FirebaseAuth

fun getUserIdFromGoogleSignIn(): String {
        return FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("User not signed in")
    }
