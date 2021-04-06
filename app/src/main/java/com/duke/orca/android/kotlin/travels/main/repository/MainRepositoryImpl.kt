package com.duke.orca.android.kotlin.travels.main.repository

import com.duke.orca.android.kotlin.travels.home.data.Tourlist
import com.duke.orca.android.kotlin.travels.network.MainApi
import timber.log.Timber
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val tourService: MainApi.TourService): MainRepository {
    override suspend fun getData(): Tourlist? {
        return try {
            tourService.getDataAsync().await()
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }
}