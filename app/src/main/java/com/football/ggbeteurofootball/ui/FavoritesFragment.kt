package com.football.ggbeteurofootball.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.adapters.AdapterFavorites
import com.football.ggbeteurofootball.databinding.FragmentFavoritesBinding
import com.football.ggbeteurofootball.listeners.FavoriteMatchSortListener
import com.football.ggbeteurofootball.models.Response

class FavoritesFragment : Fragment(), FavoriteMatchSortListener {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel = MainViewModel
    private val favoriteMatches = mutableListOf<Response>()
    private lateinit var priorityMap: MutableMap<Int, Int>
    private lateinit var adapter: AdapterFavorites
    private val TAG = "FavoritesFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding
            .bind(inflater.inflate(R.layout.fragment_favorites, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        hidePlaceholder()
        findAllFavoriteMatchesInFootballs()
        recycler()
        listeners()

    }






    private fun hidePlaceholder() {
        if (viewModel.favoriteMatches.isNotEmpty())
            binding.textPlaceholder.isVisible = false
    }








    private fun listeners() {
        binding.fab.setOnClickListener {
            binding.recyclerFavorites.scrollToPosition(0)
        }

        binding.drawerButton.setOnClickListener {
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.open()
        }
    }







    private fun recycler() {
        val layoutManager = LinearLayoutManager(requireContext())
        adapter = AdapterFavorites(
            favoriteMatches,
            getPriorityMap(),
            this
        )
        binding.recyclerFavorites.adapter = adapter
        binding.recyclerFavorites.layoutManager = layoutManager
        binding.recyclerFavorites.setOnScrollChangeListener { _, _, _, _, _ ->
            if (layoutManager.findFirstVisibleItemPosition() > 0) binding.fab.show()
            else binding.fab.hide()
        }
    }







    private fun getPriorityMap(): MutableMap<Int, Int> {
        val prioritiesMap = mutableMapOf<Int, Int>()
        val abbreviationPriorities = mapOf(
            "TBD" to 2,
            "NS" to 2,
            "1H" to 1,
            "HT" to 1,
            "2H" to 1,
            "ET" to 1,
            "BT" to 1,
            "SUSP" to 1,
            "INT" to 1,
            "FT" to 3,
            "AET" to 3,
            "PEN" to 3,
            "PST" to 2,
            "CANC" to 2,
            "ABD" to 2,
            "AWD" to 2,
            "WO" to 2,
            "LIVE" to 1,
            "P" to 1
        )

        for (data in favoriteMatches) {
            val status = data.fixture.status.short

            val priority = abbreviationPriorities.entries.firstOrNull { entry ->
                status.contains(entry.key, ignoreCase = true)
            }?.value ?: 0
            prioritiesMap[data.fixture.id] = priority
        }

        return prioritiesMap
    }







    private fun findAllFavoriteMatchesInFootballs() {
        for (football in viewModel.listLoadedFootball) {
            if (football != null) {
                for (response in football.response)
                    if (viewModel.favoriteMatches.contains(response.fixture.id))
                        favoriteMatches.add(response)
            }
        }
        priorityMap = getPriorityMap()
    }







    override fun onSortButtonPressed(index: Int) {
        val sortedList = when (index) {
            0 -> { favoriteMatches.filter { item -> priorityMap[item.fixture.id] == 2 } }
            1 -> { favoriteMatches.filter { item -> priorityMap[item.fixture.id] == 1 } }
            else -> { favoriteMatches.filter { item -> priorityMap[item.fixture.id] == 3 } }
        }
        adapter.setNewSortedList(sortedList)
    }
}