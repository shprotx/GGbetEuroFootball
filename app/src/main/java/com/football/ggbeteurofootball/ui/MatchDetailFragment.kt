package com.football.ggbeteurofootball.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.adapters.AdapterMatchWithoutStatistic
import com.football.ggbeteurofootball.data.ItemH2H
import com.football.ggbeteurofootball.databinding.FragmentMatchDetailedBinding

class MatchDetailFragment : Fragment() {

    private lateinit var binding: FragmentMatchDetailedBinding
    private val viewModel = MainViewModel

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

        if (viewModel.currentDayMatches[viewModel.currentMatchPosition].type == 2)
            showWithStatistic()
        else
            showWithH2H()

        listeners()
    }







    private fun showWithStatistic() {

    }







    private fun showWithH2H() {
        val adapter = AdapterMatchWithoutStatistic(
            listOf<ItemH2H>(), viewModel.currentDayMatches[viewModel.currentMatchPosition])
        binding.recyclerMatchDetail.adapter = adapter
    }








    private fun listeners() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }









}