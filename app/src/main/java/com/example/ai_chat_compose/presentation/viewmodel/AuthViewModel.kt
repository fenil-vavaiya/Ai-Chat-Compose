package com.example.ai_chat_compose.presentation.viewmodel

import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai_chat_compose.data.repo.AuthRepository
import com.example.ai_chat_compose.domain.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signInResult = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signInResult: StateFlow<Resource<FirebaseUser>?> = _signInResult

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _signInResult.value = Resource.Loading
            _signInResult.value = authRepository.signInWithGoogle(idToken)
        }
    }

    suspend fun getSignInIntent(): IntentSender? {
        return authRepository.getSignInIntent()
    }
}

