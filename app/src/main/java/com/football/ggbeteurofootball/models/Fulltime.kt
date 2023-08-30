package com.football.ggbeteurofootball.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Fulltime(
    val away: Int?,
    val home: Int?
)