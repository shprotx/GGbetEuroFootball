package com.football.ggbeteurofootball.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Halftime(
    val away: Int?,
    val home: Int?
)