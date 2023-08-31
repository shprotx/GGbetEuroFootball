package com.football.ggbeteurofootball.models.football

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Parameters(
    val date: String
)