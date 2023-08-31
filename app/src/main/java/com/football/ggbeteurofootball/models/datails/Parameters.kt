package com.football.ggbeteurofootball.models.datails

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Parameters(
    val fixture: String
)