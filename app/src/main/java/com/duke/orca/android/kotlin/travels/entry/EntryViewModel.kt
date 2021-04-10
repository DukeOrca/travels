package com.duke.orca.android.kotlin.travels.entry

import android.app.Activity
import android.content.Context
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duke.orca.android.kotlin.travels.entry.login.data.LoginResponse
import com.duke.orca.android.kotlin.travels.entry.login.repository.LoginRepositoryImpl
import com.duke.orca.android.kotlin.travels.entry.network.EntryApi
import com.duke.orca.android.kotlin.travels.entry.sign_up.data.SignUpResponse
import com.duke.orca.android.kotlin.travels.entry.sign_up.repository.SignUpRepository
import com.duke.orca.android.kotlin.travels.entry.sign_up.repository.SignUpRepositoryImpl
import com.facebook.CallbackManager
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor() : ViewModel() {
    private val callbackManager = CallbackManager.Factory.create()
    private val loginRepository = LoginRepositoryImpl()
    private val signUpRepository = SignUpRepositoryImpl()

    fun callbackManager(): CallbackManager = callbackManager

    fun loginWithEmail(
        email: String,
        password: String,
        onSuccess: (response: LoginResponse) -> Unit,
        onFailure: (message: String) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loginRepository.loginWithEmail(email, password, {
                    onSuccess(it)
                }, {
                    onFailure(it)
                })
            }
        }
    }

    fun loginWithFacebook(
        fragment: Fragment,
        @MainThread onSuccess: (token: String) -> Unit,
        @MainThread onFailure: (Throwable?) -> Unit
    ) {
        loginRepository.loginWithFacebook(callbackManager, fragment, onSuccess, onFailure)
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

    fun signUp(email: String, password: String,
               onSuccess: (response: SignUpResponse) -> Unit,
               onFailure: (Throwable?) -> Unit
    ) {
        val signUpBody = EntryApi.createSignUpBody(
            email = email,
            password = password,
            provider = EntryApi.Provider.Standard,
            token = "token",
            nickname = "nickname"
        )

        viewModelScope.launch {
            try {
                onSuccess(signUpRepository.signUp(signUpBody))
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}
