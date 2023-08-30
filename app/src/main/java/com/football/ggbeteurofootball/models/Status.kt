package com.football.ggbeteurofootball.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Status(
    val elapsed: Int?,
    val long: String,
    val short: String
)