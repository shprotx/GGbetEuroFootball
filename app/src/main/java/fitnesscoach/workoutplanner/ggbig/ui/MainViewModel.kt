package fitnesscoach.workoutplanner.ggbig.ui

import androidx.lifecycle.ViewModel
import fitnesscoach.workoutplanner.ggbig.data.ItemDay
import fitnesscoach.workoutplanner.ggbig.models.football.Football
import fitnesscoach.workoutplanner.ggbig.models.football.Response

object MainViewModel: ViewModel() {

    val days = mutableListOf<ItemDay>()
    val listLoadedFootball = MutableList<Football?>(7){null}

    val currentDayMatches = mutableListOf<Response>()
    var currentDay = -1
    var currentMatchId = 0
    var currentType = -1
    var currentPriorityMap = mutableMapOf<Int, Int>()

    var favoriteMatches = mutableListOf<Int>()
    var favoriteMatchesList = mutableListOf<Response>()

    var placeholderSize1 = 0
    var placeholderSize2 = 0

    lateinit var favoritesAdapter: fitnesscoach.workoutplanner.ggbig.adapters.AdapterFavorites

}