package com.duke.orca.android.kotlin.travels.schedule.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Suppress("SpellCheckingInspection")
@Parcelize
data class Schedule (
        val address: String,
        val audioUrl: String,
        val detailedDescription: String,
        val dropLocation: Location?,
        val oneLineDescription: String,
        val phoneNumber: String,
        val pickUpLocation: Location?,
        val tourimg: String,
        val tourspotname: String,
        val tourbegindate: String,
        val tourbegintime: String,
        val tourhour: Int,
        val tourLocation: Location,
        val videoUrl: String
) : Parcelable

@Parcelize
data class Location(
        val latitude: Double,
        val longitude: Double
) : Parcelable