package com.duke.orca.android.kotlin.travels.entry.login.repository

import android.app.Activity
import android.content.Context
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import com.duke.orca.android.kotlin.travels.entry.login.data.LoginResponse
import com.facebook.CallbackManager
import com.kakao.sdk.auth.model.OAuthToken

interface LoginRepository {
    suspend fun loginWithEmail(
        email: String,
        password: String,
        @MainThread onSuccess: (response: LoginResponse) -> Unit,
        @MainThread onFailure: (message: String) -> Unit
    )

    fun loginWithFacebook(
        callbackManager: CallbackManager,
        fragment: Fragment,
        @MainThread onSuccess: (token: String) -> Unit,
        @MainThread onFailure: (Throwable?) -> Unit
    )

    fun loginWithGoogle(
        activity: Activity,
        @MainThread onFailure: (Throwable?) -> Unit
    )

    @Suppress("SpellCheckingInspection")
    fun loginWithKakao(
        context: Context,
        @MainThread onSuccess: (token: OAuthToken) -> Unit,
        @MainThread onFailure: (Throwable?) -> Unit
    )

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
    )
}