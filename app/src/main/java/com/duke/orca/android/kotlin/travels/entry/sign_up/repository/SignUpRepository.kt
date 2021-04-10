package com.duke.orca.android.kotlin.travels.entry.sign_up.repository

import androidx.annotation.MainThread
import com.duke.orca.android.kotlin.travels.entry.sign_up.data.SignUpResponse

interface SignUpRepository {
    suspend fun signUp(
        body: HashMap<String, String>,
    ): SignUpResponse
}