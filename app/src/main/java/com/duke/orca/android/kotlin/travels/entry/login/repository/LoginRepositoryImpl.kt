package com.duke.orca.android.kotlin.travels.entry.login.repository

import android.app.Activity
import android.content.Context
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import com.duke.orca.android.kotlin.travels.R
import com.duke.orca.android.kotlin.travels.entry.login.data.LoginResponse
import com.duke.orca.android.kotlin.travels.entry.network.EntryApi
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.NullPointerException
import java.lang.ref.WeakReference

class LoginRepositoryImpl: LoginRepository {
    object RequestCode {
        const val GoogleSignIn = 105
    }

    /** Email */
    override suspend fun loginWithEmail(
        email: String,
        password: String,
        onSuccess: (data: LoginResponse) -> Unit,
        onFailure: (message: String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response = EntryApi.loginService().getLoginAsync(
                    hashMapOf(
                        "email" to email,
                        "password" to password
                    )
                ).await()

                withContext(Dispatchers.Main) {
                    if (response.success)
                        onSuccess(response)
                    else
                        onFailure(response.message)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure("Email login failed.")
                }
            }
        }
    }

    /** Facebook */
    override fun loginWithFacebook(
        callbackManager: CallbackManager,
        fragment: Fragment,
        @MainThread onSuccess: (token: String) -> Unit,
        @MainThread onFailure: (Throwable?) -> Unit
    ) {
        LoginManager.getInstance().apply {
            loginBehavior = LoginBehavior.NATIVE_WITH_FALLBACK
            logInWithReadPermissions(fragment, listOf("public_profile", "email"))
            registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        result?.accessToken?.token?.let {
                            onSuccess(it)
                        } ?: run {
                            onFailure(NullPointerException("token: null"))
                        }
                    }

                    override fun onCancel() {
                        Timber.w("onCancel")
                    }

                    override fun onError(error: FacebookException?) {
                        onFailure(error)
                    }
                })
        }
    }

    /** Google */
    override fun loginWithGoogle(
        activity: Activity,
        @MainThread onFailure: (Throwable?) -> Unit
    ) {
        try {
            val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.client_id))
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions)
            val signInIntent = googleSignInClient?.signInIntent

            activity.startActivityForResult(signInIntent, RequestCode.GoogleSignIn)
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    /** Kakao */
    @Suppress("SpellCheckingInspection")
    override fun loginWithKakao(
        context: Context,
        @MainThread onSuccess: (token: OAuthToken) -> Unit,
        @MainThread onFailure: (Throwable?) -> Unit
    ) {
        val compositeDisposable = CompositeDisposable()

        Single.just(UserApiClient.instance.isKakaoTalkLoginAvailable(context))
            .flatMap { available ->
                if (available)
                    UserApiClient.rx.loginWithKakaoTalk(context)
                else
                    UserApiClient.rx.loginWithKakaoAccount(context)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ token ->
                onSuccess(token)
                compositeDisposable.dispose()
            }, { error ->
                onFailure(error)
                compositeDisposable.dispose()
            })
            .addTo(compositeDisposable)
    }

    /** Naver */
    @Suppress("SpellCheckingInspection")
    private object NaverApi {
        const val ClientId = "QTFpbJsDkwRRou7ZL0HC"
        const val ClientSecret = "eYL2DyDitG"
    }

    @Suppress("SpellCheckingInspection")
    override fun loginWithNaver(
        activity: Activity,
        @MainThread onSuccess: (
            accessToken: String,
            refreshToken: String,
            expiresAt: Long,
            tokenType: String
        ) -> Unit,
        @MainThread onFailure: (errorCode: String, errorDesc: String) -> Unit
    ) {
        val oAuthLogin = OAuthLogin.getInstance()
        oAuthLogin.init(
            activity,
            NaverApi.ClientId,
            NaverApi.ClientSecret,
            NaverApi.ClientId
        )

        oAuthLogin.startOauthLoginActivity(
            activity,
            WeakReferenceOAuthLoginHandler(activity, oAuthLogin, onSuccess, onFailure)
        )
    }

    private class WeakReferenceOAuthLoginHandler(
        private val activity: Activity,
        private val oAuthLogin: OAuthLogin,
        @MainThread private val onSuccess: (
            accessToken: String,
            refreshToken: String,
            expiresAt: Long,
            tokenType: String
        ) -> Unit,
        @MainThread private val onFailure: (errorCode: String, errorDesc: String) -> Unit
    ): OAuthLoginHandler() {
        private val weakReference = WeakReference(activity)

        override fun run(success: Boolean) {
            if (success) {
                val accessToken: String = oAuthLogin.getAccessToken(activity)
                val refreshToken: String = oAuthLogin.getRefreshToken(activity)
                val expiresAt: Long = oAuthLogin.getExpiresAt(activity)
                val tokenType: String = oAuthLogin.getTokenType(activity)

                onSuccess(accessToken, refreshToken, expiresAt, tokenType)
            } else {
                val errorCode: String = oAuthLogin.getLastErrorCode(activity).code
                val errorDesc: String = oAuthLogin.getLastErrorDesc(activity)

                onFailure(errorCode, errorDesc)
            }
        }
    }
}