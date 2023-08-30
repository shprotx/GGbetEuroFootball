package com.football.ggbeteurofootball.ui

import androidx.lifecycle.ViewModel
import com.football.ggbeteurofootball.data.ItemDay
import com.football.ggbeteurofootball.data.ItemMatch
import com.football.ggbeteurofootball.models.Football

object MainViewModel: ViewModel() {

    val days = mutableListOf<ItemDay>()
    val listLoadedFootball = mutableListOf<Football?>()

    val currentDayMatches = mutableListOf<ItemMatch>()
    var currentMatchPosition = 0
}