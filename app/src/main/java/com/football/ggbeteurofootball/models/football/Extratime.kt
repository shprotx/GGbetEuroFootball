package com.football.ggbeteurofootball.models.football

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Extratime(
    val away: Int?,
    val home: Int?
)