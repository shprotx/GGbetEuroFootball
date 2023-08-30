package com.football.ggbeteurofootball.listeners

interface MatchesListListener {

    fun onAnotherDaySelected(day: Int)

    fun onMatchClicked(id: Int, position: Int)

}