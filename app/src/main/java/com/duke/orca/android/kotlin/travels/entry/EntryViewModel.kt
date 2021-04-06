package com.duke.orca.android.kotlin.travels.entry

import android.app.Activity
import android.content.Context
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import com.duke.orca.android.kotlin.travels.entry.login.repository.LoginRepositoryImpl
import com.facebook.CallbackManager
import com.kakao.sdk.auth.model.OAuthToken
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor() : ViewModel() {
    private val callbackManager = CallbackManager.Factory.create()
    private val loginRepository = LoginRepositoryImpl()
    
    fun callbackManager(): CallbackManager = callbackManager

    fun loginWithFacebook(
        activity: Activity,
        @MainThread onSuccess: (token: String) -> Unit,
        @MainThread onFailure: (Throwable?) -> Unit
    ) {
        loginRepository.loginWithFacebook(activity, callbackManager, onSuccess, onFailure)
    }

    fun loginWithGoogle(
        activity: Activity,
        @MainThread onFailure: (Throwable?) -> Unit
    ) {
        loginRepository.loginWithGoogle(activity, onFailure)
    }

    @Suppress("SpellCheckingInspection")
    fun loginWithKakao(
        context: Context,
        @MainThread onSuccess: (token: OAuthToken) -> Unit,
        @MainThread onFailure: (Throwable?) -> Unit
    ) {
        loginRepository.loginWithKakao(context, onSuccess, onFailure)
    }

    @Suppress("SpellCheckingInspection")
    fun loginWithNaver(
        activity: Activity,
        @MainThread onSuccess: (
            accessToken: String,
            refreshToken: String,
            expiresAt: Long,
            tokenType: String
        ) -> Unit,
        @MainThread onFailure: (errorCode: String, errorDesc: String) -> Unit
    ) {
        loginRepository.loginWithNaver(activity, onSuccess, onFailure)
    }
}
