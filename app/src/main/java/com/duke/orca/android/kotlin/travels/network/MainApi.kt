package com.duke.orca.android.kotlin.travels.network

import com.duke.orca.android.kotlin.travels.home.data.Tourlist
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainApi {
    private const val BASE_URL = "https://azanghs.cafe24.com/itstudy/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
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

    @Provides
    @Singleton
    fun provideTourService(retrofit: Retrofit): TourService {
        return retrofit.create(TourService::class.java)
    }

    interface TourService {
        @Suppress("SpellCheckingInspection")
        @Headers("Content-Type: application/json")
        @POST("tourlist.php")
        fun getDataAsync(
            @Body body: HashMap<String, Int> = hashMapOf( "useridx" to 1),
        ): Deferred<Tourlist>
    }
}