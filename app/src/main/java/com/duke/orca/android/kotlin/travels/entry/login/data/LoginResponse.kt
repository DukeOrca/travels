package com.duke.orca.android.kotlin.travels.entry.login.data

data class LoginResponse (
    val success: Boolean,
    val data: LoginData,
    val message: String
)

data class LoginData (
    val token: String,
    val refreshToken: String,
    val email: String,
    @Suppress("SpellCheckingInspection")
    val useridx: Int,
    val nickname: String
)