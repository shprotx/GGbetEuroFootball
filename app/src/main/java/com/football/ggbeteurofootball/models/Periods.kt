package com.football.ggbeteurofootball.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Periods(
    val first: Int?,
    val second: Int?
)