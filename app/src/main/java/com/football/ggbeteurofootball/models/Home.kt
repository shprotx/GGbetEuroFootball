package com.football.ggbeteurofootball.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Home(
    val id: Int,
    val logo: String,
    val name: String,
    val winner: Boolean?
)