package com.duke.orca.android.kotlin.travels.schedule.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Suppress("SpellCheckingInspection")
@Parcelize
data class Schedule (
        val address: String,
        val audioUrl: String,
        val detailedDescription: String,
        val oneLineDescription: String,
        val phoneNumber: String,
        val tourimg: String,
        val tourspotname: String,
        val tourbegindate: String,
        val tourbegintime: String,
        val tourhour: Int
): Parcelable