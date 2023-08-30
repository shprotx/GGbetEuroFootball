package com.football.ggbeteurofootball.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.football.ggbeteurofootball.adapters.AdapterMatches
import com.football.ggbeteurofootball.data.ItemDay
import com.football.ggbeteurofootball.data.ItemMatch
import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.databinding.FragmentMatchesBinding
import com.football.ggbeteurofootball.listeners.MatchesListListener

class MatchesFragment : Fragment(), MatchesListListener {

    private lateinit var binding: FragmentMatchesBinding
    private lateinit var adapter: AdapterMatches
    private val viewModel = MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchesBinding
            .bind(inflater.inflate(R.layout.fragment_matches, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectMatchesForSelectedDay(3) // current day position is 3
        recycler()
        listeners()

    }







    private fun listeners() {
        binding.fab.setOnClickListener {
            binding.recyclerMatches.scrollToPosition(0)
        }

        binding.drawerButton.setOnClickListener {
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.open()
        }
    }









    private fun collectMatchesForSelectedDay(day: Int) {
        if (viewModel.listLoadedFootball.isNotEmpty()) {
            viewModel.currentDayMatches.clear()

            for (i in 0 until viewModel.listLoadedFootball[day]!!.results) {
                val item = viewModel.listLoadedFootball[day]!!.response[i]
                val id = item.fixture.id
                val type = when (item.fixture.status.short) {
                                "NS" -> 3
                                "TBD" -> 3
                                "PST" -> 3
                                "CANC" -> 3
                                "ABD" -> 3
                                "AWD" -> 3
                                "WO" -> 3
                                "FT" -> 2
                                "AET" -> 2
                                "PEN" -> 2
                                else -> 1
                }
                val league = item.league.name
                val homeTeamLogo = item.teams.home.logo
                val homeTeamName = item.teams.home.name
                val homeTeamScore = item.goals.home
                val awayTeamLogo = item.teams.away.logo
                val awayTeamName = item.teams.away.name
                val awayTeamScore = item.goals.away
                viewModel.currentDayMatches.add(
                    ItemMatch(
                        id,
                        type,
                        league,
                        homeTeamLogo,
                        homeTeamName,
                        homeTeamScore,
                        awayTeamLogo,
                        awayTeamName,
                        awayTeamScore
                    )
                )
            }
            viewModel.currentDayMatches.sortBy { it.type }
        }
    }








    private fun recycler() {
        val layoutManager = LinearLayoutManager(requireContext())
        adapter = AdapterMatches(viewModel.currentDayMatches, viewModel.days, this)
        binding.recyclerMatches.adapter = adapter
        binding.recyclerMatches.layoutManager = layoutManager
        binding.recyclerMatches.setOnScrollChangeListener { _, _, _, _, _ ->
            if (layoutManager.findFirstVisibleItemPosition() > 0) binding.fab.show()
            else binding.fab.hide()
        }
    }







    override fun onAnotherDaySelected(day: Int) {
        collectMatchesForSelectedDay(day)
        adapter.setNewList(viewModel.currentDayMatches, day)
    }







    override fun onMatchClicked(id: Int, position: Int) {
        viewModel.currentMatchPosition = position
        findNavController().navigate(R.id.matchDetailFragment)
    }


}