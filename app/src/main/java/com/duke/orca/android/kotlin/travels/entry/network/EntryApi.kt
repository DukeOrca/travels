package com.duke.orca.android.kotlin.travels.entry.network

import com.duke.orca.android.kotlin.travels.entry.login.data.LoginResponse
import com.duke.orca.android.kotlin.travels.entry.sign_up.data.SignUpResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object EntryApi {
    object Provider {
        const val Facebook = "F"
        const val Google = "G"
        @Suppress("SpellCheckingInspection")
        const val Kakao = "K"
        @Suppress("SpellCheckingInspection")
        const val Naver = "N"
        const val Standard = "S"
    }

    private const val BASE_URL = "https://azanghs.cafe24.com/itstudy/"

    private fun createRetrofit(): Retrofit {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build()
    }

    interface LoginService {
        @Headers("Content-Type: application/json")
        @POST("login.php")
        fun getLoginAsync(
            @Body body: HashMap<String, String>
        ): Deferred<LoginResponse>
    }

    interface SignUpService {
        @Suppress("SpellCheckingInspection")
        @Headers("Content-Type: application/json")
        @POST("signup.php")
        fun getSignUpAsync(
            @Body body: HashMap<String, String>
        ): Deferred<SignUpResponse>
    }

    fun createSignUpBody(email: String, password: String, provider: String, token: String, nickname: String): HashMap<String, String> {
        return hashMapOf(
            "email" to email,
            "password" to password,
            "provider" to provider,
            "token" to token,
            "nickname" to nickname
        )
    }

    fun loginService(): LoginService = createRetrofit().create(LoginService::class.java)
    fun signUpService(): SignUpService = createRetrofit().create(SignUpService::class.java)
}