package com.duke.orca.android.kotlin.travels.home.data

import java.util.*

@Suppress("SpellCheckingInspection")
data class Tour(
        val tourimg: String,
        val tourspotname: String,
        val tourbegindate: Date,
        val tourbegintime: String,
        val tourhour: Int
)