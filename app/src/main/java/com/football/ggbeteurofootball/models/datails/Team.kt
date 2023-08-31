package com.football.ggbeteurofootball.models.datails

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Team(
    val id: Int,
    val logo: String,
    val name: String
)