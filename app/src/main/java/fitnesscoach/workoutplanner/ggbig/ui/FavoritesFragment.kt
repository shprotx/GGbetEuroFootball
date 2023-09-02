package fitnesscoach.workoutplanner.ggbig.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fitnesscoach.workoutplanner.ggbig.R
import fitnesscoach.workoutplanner.ggbig.databinding.FragmentFavoritesBinding
import fitnesscoach.workoutplanner.ggbig.listeners.FavoriteMatchSortListener
import fitnesscoach.workoutplanner.ggbig.listeners.MatchesSelectedListener
import fitnesscoach.workoutplanner.ggbig.other.Checker


class FavoritesFragment : Fragment(), FavoriteMatchSortListener, MatchesSelectedListener {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel = MainViewModel
    private lateinit var priorityMap: MutableMap<Int, Int>
    private val checker = Checker
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

        if (checker.checkInternet(requireContext())) {
            hidePlaceholder()
            findAllFavoriteMatchesInFootballs()
            recycler()
            listeners()
        } else {
            findNavController().navigate(R.id.noInternetFragment)
        }

    }






    private fun hidePlaceholder() {
        binding.textPlaceholder.isVisible = viewModel.favoriteMatchesList.isEmpty()
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
        viewModel.favoritesAdapter =
            fitnesscoach.workoutplanner.ggbig.adapters.AdapterFavorites(
                viewModel.favoriteMatchesList,
                getPriorityMap(),
                this,
                this,
                viewModel.placeholderSize1,
                viewModel.placeholderSize2
            )
        binding.recyclerFavorites.adapter = viewModel.favoritesAdapter
        binding.recyclerFavorites.layoutManager = layoutManager
        binding.recyclerFavorites.setOnScrollChangeListener { _, _, _, _, _ ->
            if (layoutManager.findFirstVisibleItemPosition() > 0) binding.fab.show()
            else binding.fab.hide()
        }
    }







    private fun getPriorityMap(): MutableMap<Int, Int> {
        val prioritiesMap = mutableMapOf<Int, Int>()
        val abbreviationPriorities = mapOf(
            "TBD" to 3,
            "NS" to 3,
            "1H" to 1,
            "HT" to 1,
            "2H" to 1,
            "ET" to 1,
            "BT" to 1,
            "P" to 1,
            "SUSP" to 1,
            "INT" to 1,
            "FT" to 2,
            "AET" to 2,
            "PEN" to 2,
            "PST" to 4,
            "CANC" to 5,
            "ABD" to 5,
            "AWD" to 5,
            "WO" to 5,
            "LIVE" to 1
        )

        for (data in viewModel.favoriteMatchesList) {
            val status = data.fixture.status.short

            val priority = abbreviationPriorities.entries.firstOrNull { entry ->
                status == entry.key
            }?.value ?: 0
            prioritiesMap[data.fixture.id] = priority
        }

        return prioritiesMap
    }







    private fun findAllFavoriteMatchesInFootballs() {
        viewModel.favoriteMatchesList.clear()
        for (football in viewModel.listLoadedFootball) {
            if (football != null) {
                for (response in football.response)
                    if (viewModel.favoriteMatches.contains(response.fixture.id))
                            viewModel.favoriteMatchesList.add(response)
            }
        }

        priorityMap = getPriorityMap()
    }







    override fun onSortButtonPressed(index: Int) {
        val sortedList = when (index) {
            0 -> { viewModel.favoriteMatchesList.filter { item -> priorityMap[item.fixture.id] == 3 } }
            1 -> { viewModel.favoriteMatchesList.filter { item -> priorityMap[item.fixture.id] == 1 } }
            else -> { viewModel.favoriteMatchesList.filter { item -> priorityMap[item.fixture.id] == 2 } }
        }
        binding.textPlaceholder.isVisible = sortedList.isEmpty()
        viewModel.favoritesAdapter.setNewSortedList(sortedList)
    }






    override fun onMatchClicked(id: Int, type: Int) {
        viewModel.currentMatchId = id
        viewModel.currentType = type
        findNavController().navigate(R.id.matchDetailFragment)
    }
}