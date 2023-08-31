package com.football.ggbeteurofootball.models.football

import androidx.room.Entity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "football")
data class Football(
    val errors: List<String>,
    val `get`: String,
    val paging: Paging,
    val parameters: Parameters?,
    val response: List<Response>,
    val results: Int
)