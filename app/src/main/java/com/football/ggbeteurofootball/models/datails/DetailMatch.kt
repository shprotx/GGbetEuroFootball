package com.football.ggbeteurofootball.models.datails

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class DetailMatch(
    val errors: List<Any>,
    val `get`: String,
    val paging: Paging,
    val parameters: Parameters,
    val response: List<Response>,
    val results: Int
)