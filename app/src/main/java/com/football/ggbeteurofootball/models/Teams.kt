package com.football.ggbeteurofootball.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Teams(
    val away: Away,
    val home: Home
)