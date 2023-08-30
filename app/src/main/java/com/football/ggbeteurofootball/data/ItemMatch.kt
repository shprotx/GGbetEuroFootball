package com.football.ggbeteurofootball.data

data class ItemMatch(
    val id: Int,
    val type: Int,
    val league: String,
    val firstTeamIcon: String,
    val firstTeamName: String,
    val firstTeamScore: Int?,
    val secondTeamIcon: String,
    val secondTeamName: String,
    val secondTeamScore: Int?
)
