package com.football.ggbeteurofootball.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.football.ggbeteurofootball.data.ItemDay
import com.football.ggbeteurofootball.data.ItemMatch
import com.football.ggbeteurofootball.models.Football
import com.football.ggbeteurofootball.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object MainViewModel: ViewModel() {

    val days = mutableListOf<ItemDay>()
    val listLoadedFootball = mutableListOf<Football?>()

    val currentDayMatches = mutableListOf<Response>()
    var currentDay = -1
    var currentMatchId = 0
    var currentType = -1
    var currentPriorityMap = mutableMapOf<Int, Int>()



}