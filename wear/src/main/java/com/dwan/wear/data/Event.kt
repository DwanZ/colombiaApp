package com.dwan.wear.data

import androidx.annotation.StringRes

data class Event(
    @StringRes val title: Int,
    val text: String
)