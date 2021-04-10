package com.duke.orca.android.kotlin.travels.entry.sign_up.data

data class SignUpResponse(
    val success: Boolean,
    val data: SignUpData,
    val message: String
)

data class SignUpData(
    val email: String,
    @Suppress("SpellCheckingInspection")
    val useridx: Int
)