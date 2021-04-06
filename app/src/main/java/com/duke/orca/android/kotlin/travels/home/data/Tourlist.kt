package com.duke.orca.android.kotlin.travels.home.data

@Suppress("SpellCheckingInspection")
data class Tourlist (
    val data: Data
)

data class Data (
    @Suppress("SpellCheckingInspection")
    val useridx: Int,
    @Suppress("SpellCheckingInspection")
    val tourlist: List<Tour>
)