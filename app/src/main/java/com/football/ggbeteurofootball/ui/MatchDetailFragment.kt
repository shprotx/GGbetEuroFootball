package com.football.ggbeteurofootball.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.adapters.AdapterMatchWithoutStatistic
import com.football.ggbeteurofootball.data.ItemH2H
import com.football.ggbeteurofootball.databinding.FragmentMatchDetailedBinding
import com.football.ggbeteurofootball.models.Response

class MatchDetailFragment : Fragment() {

    private lateinit var binding: FragmentMatchDetailedBinding
    private val viewModel = MainViewModel
    private val TAG = "MatchDetailFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchDetailedBinding
            .bind(inflater.inflate(R.layout.fragment_match_detailed, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.currentType == 2)
            showWithStatistic()
        else
            showWithH2H()

        listeners()
    }







    private fun showWithStatistic() {

    }







    private fun showWithH2H() {
        var response: Response? = viewModel.currentDayMatches.find { it.fixture.id == viewModel.currentMatchId }
        Log.d(TAG, "showWithH2H: ${response!!.teams.home.logo}")
        Log.d(TAG, "showWithH2H: ${response!!.teams.away.logo}")
        val adapter = AdapterMatchWithoutStatistic(
            listOf<ItemH2H>(), response!!, viewModel.currentType)
        binding.recyclerMatchDetail.adapter = adapter
    }








    private fun listeners() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }









}