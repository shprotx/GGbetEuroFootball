package com.football.ggbeteurofootball.ui

import android.content.SharedPreferences.Editor
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.adapters.AdapterMatchWithoutStatistic
import com.football.ggbeteurofootball.api.FootballApiImplementation
import com.football.ggbeteurofootball.data.ItemH2H
import com.football.ggbeteurofootball.databinding.FragmentMatchDetailedBinding
import com.football.ggbeteurofootball.models.football.Response
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MatchDetailFragment : Fragment() {

    @Inject
    lateinit var editor: Editor
    @Inject
    lateinit var apiImplementation: FootballApiImplementation
    private lateinit var binding: FragmentMatchDetailedBinding
    private val viewModel = MainViewModel
    private val response: Response? =
        viewModel.currentDayMatches.find { it.fixture.id == viewModel.currentMatchId }
    private var isMatchInFavorites = false

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

        someBaseSettings()

        if (viewModel.currentType == 2)
            showWithStatistic()
        else
            showWithH2H()

        listeners(view)
    }


    private fun someBaseSettings() {
        isMatchInFavorites = if (viewModel.favoriteMatches.contains(viewModel.currentMatchId)) {
            binding.favoriteButton.setImageResource(R.drawable.star_checked)
            true
        } else {
            binding.favoriteButton.setImageResource(R.drawable.star_unchecked)
            false
        }
    }


    private fun showWithStatistic() {
        binding.progressBarMain.isVisible = true
        lifecycleScope.launch(Dispatchers.IO) {
            binding.progressBarMain.isVisible = false
            binding.progressBarMain.progress = 50
            apiImplementation.getDetailMatchByFixture(viewModel.currentMatchId) {
                if (it != null) {
                    // Show Statistic
                } else {
                    binding.progressBarMain.isVisible = false
                    showWithH2H()
                }

            }
        }

    }


    private fun showWithH2H() {
        Log.d(TAG, "showWithH2H score: ${response?.score}")

        if (response != null) {
            val h2h = mutableListOf<ItemH2H>()
            if (response.score.halftime.home != null)
                h2h.add(
                    ItemH2H(
                        response.teams.home.logo,
                        response.teams.away.logo,
                        convertDateFormat(response.fixture.date),
                        response.score.halftime.home!!,
                        response.score.halftime.away!!
                    )
                )
            if (response.score.fulltime.home != null)
                h2h.add(
                    ItemH2H(
                        response.teams.home.logo,
                        response.teams.away.logo,
                        convertDateFormat(response.fixture.date),
                        response.score.fulltime.home!!,
                        response.score.fulltime.away!!
                    )
                )
            if (response.score.extratime.home != null)
                h2h.add(
                    ItemH2H(
                        response.teams.home.logo,
                        response.teams.away.logo,
                        convertDateFormat(response.fixture.date),
                        response.score.extratime.home!!,
                        response.score.extratime.away!!
                    )
                )
            if (response.score.penalty.home != null)
                h2h.add(
                    ItemH2H(
                        response.teams.home.logo,
                        response.teams.away.logo,
                        convertDateFormat(response.fixture.date),
                        response.score.penalty.home!!,
                        response.score.penalty.away!!
                    )
                )

            Log.d(TAG, "showWithH2H h2h: $h2h")
            val adapter = AdapterMatchWithoutStatistic(
                requireContext(),
                h2h,
                response,
                viewModel.currentType,
                viewModel.placeholderSize1,
                viewModel.placeholderSize2
            )
            binding.recyclerMatchDetail.adapter = adapter
            Log.d(TAG, "showWithH2H: ${response.fixture.status.short}")
        } else
            findNavController().navigate(R.id.noInternetFragment)


    }


    private fun convertDateFormat(inputDate: String): String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH)
        val outputDateFormat = SimpleDateFormat("EEEE d MMMM yyyy", Locale.ENGLISH)

        val date: Date = inputDateFormat.parse(inputDate) ?: return ""

        return outputDateFormat.format(date)
    }


    private fun listeners(view: View) {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.favoriteButton.setOnClickListener {
            if (response != null) {
                isMatchInFavorites = !isMatchInFavorites
                if (isMatchInFavorites) {
                    viewModel.favoriteMatches.add(viewModel.currentMatchId)
                    binding.favoriteButton.setImageResource(R.drawable.star_checked)
                    showSnackBar(view, "Match was saved to favorites", "Cancel") {
                        viewModel.favoriteMatches.remove(viewModel.currentMatchId)
                        binding.favoriteButton.setImageResource(R.drawable.star_unchecked)
                        isMatchInFavorites = !isMatchInFavorites
                    }
                } else {
                    viewModel.favoriteMatches.remove(viewModel.currentMatchId)
                    binding.favoriteButton.setImageResource(R.drawable.star_unchecked)
                    showSnackBar(view, "Match was deleted from favorites", "Cancel") {
                        viewModel.favoriteMatches.add(viewModel.currentMatchId)
                        binding.favoriteButton.setImageResource(R.drawable.star_checked)
                        isMatchInFavorites = !isMatchInFavorites
                    }
                }
            } else {
                showSnackBar(view, "Something went wrong. Try again", "Retry") {
                    //
                }
            }

        }
    }


    private fun showSnackBar(
        view: View,
        message: String,
        button: String,
        toDo: () -> Unit
    ) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setActionTextColor(Color.parseColor("#FE8001"))
            .setBackgroundTint(Color.parseColor("#2A3040"))
            .setTextColor(Color.WHITE)
            .setAction(button) {
                toDo
            }.show()
    }


    private fun intListToString(intList: MutableList<Int>): String {
        return intList.joinToString(",")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val favorites = intListToString(viewModel.favoriteMatches)
        editor.putString("favorite", favorites)
        editor.apply()
    }
}