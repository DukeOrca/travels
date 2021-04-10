package com.duke.orca.android.kotlin.travels.entry.sign_up.repository

import com.duke.orca.android.kotlin.travels.entry.network.EntryApi
import com.duke.orca.android.kotlin.travels.entry.sign_up.data.SignUpResponse

class SignUpRepositoryImpl: SignUpRepository {
    override suspend fun signUp(body: HashMap<String, String>): SignUpResponse {
        return EntryApi.signUpService().getSignUpAsync(body).await()
    }
}