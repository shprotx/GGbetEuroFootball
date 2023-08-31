package com.football.ggbeteurofootball.models.football

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Penalty(
    val away: Int?,
    val home: Int?
)