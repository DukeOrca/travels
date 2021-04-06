package com.duke.orca.android.kotlin.travels.main.repository

import com.duke.orca.android.kotlin.travels.home.data.Tourlist

interface MainRepository {
    suspend fun getData(): Tourlist?
}