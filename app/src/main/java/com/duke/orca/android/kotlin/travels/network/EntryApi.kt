package com.duke.orca.android.kotlin.travels.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

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

    }

    interface SignUpService {
        @Suppress("SpellCheckingInspection")
        @GET("signup.php")
        fun getSignUpAsync(
            @Query("email") email: String,
            @Query("password") password: String,
            @Query("provider") provider: String = "N",
            @Query("token") token: String = "testtoken",
            @Query("nickname") nickname: String = "nickname"
        ): Deferred<Response>
    }

    fun signUpService(): SignUpService = createRetrofit().create(SignUpService::class.java)
}

data class Response(
    val success: Boolean,
    val data: String,
    val message: String
)