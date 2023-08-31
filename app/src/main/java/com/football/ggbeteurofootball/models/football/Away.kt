package com.football.ggbeteurofootball.models.football

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Away(
    val id: Int,
    val logo: String,
    val name: String,
    val winner: Boolean?
)