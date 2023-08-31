package com.football.ggbeteurofootball.ui

import androidx.lifecycle.ViewModel
import com.football.ggbeteurofootball.data.ItemDay
import com.football.ggbeteurofootball.models.Football
import com.football.ggbeteurofootball.models.Response

object MainViewModel: ViewModel() {

    val days = mutableListOf<ItemDay>()
    val listLoadedFootball = MutableList<Football?>(7){null}

    val currentDayMatches = mutableListOf<Response>()
    var currentDay = -1
    var currentMatchId = 0
    var currentType = -1
    var currentPriorityMap = mutableMapOf<Int, Int>()

    var favoriteMatches = mutableListOf<Int>()

}