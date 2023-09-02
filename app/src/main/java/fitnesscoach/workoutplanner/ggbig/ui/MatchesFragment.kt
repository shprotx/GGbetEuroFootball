package fitnesscoach.workoutplanner.ggbig.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fitnesscoach.workoutplanner.ggbig.R
import fitnesscoach.workoutplanner.ggbig.adapters.AdapterMatches
import fitnesscoach.workoutplanner.ggbig.databinding.FragmentMatchesBinding
import fitnesscoach.workoutplanner.ggbig.listeners.DaySelectedListener
import fitnesscoach.workoutplanner.ggbig.listeners.MatchesSelectedListener
import fitnesscoach.workoutplanner.ggbig.other.Checker

class MatchesFragment : Fragment(), MatchesSelectedListener, DaySelectedListener {

    private lateinit var binding: FragmentMatchesBinding
    private lateinit var adapter: AdapterMatches
    private val checker = Checker
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

        if (checker.checkInternet(requireContext())) {
            collectMatchesForSelectedDay(viewModel.currentDay) // current day position is 3
            recycler()
            listeners()
        } else {
            findNavController().navigate(R.id.noInternetFragment)
        }

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


    /**
     *  Метод нужен для сортировки списка перед передачей его ресайклеру. Задаются приоритеты на
     *  основании категорий, ожидаемых от АПИ. Потом список для конкретного дня сортируется по этим
     *  приоритетам и сохраняется во вьюМодел.
     */
    private fun collectMatchesForSelectedDay(day: Int) {
        if (viewModel.listLoadedFootball.isNotEmpty()) {
            viewModel.currentDayMatches.clear()

            val prioritiesMap = mutableMapOf<Int, Int>()
            val abbreviationPriorities = mapOf(
                "TBD" to 3,
                "NS" to 3,
                "1H" to 1,
                "HT" to 1,
                "2H" to 1,
                "ET" to 1,
                "BT" to 1,
                "SUSP" to 1,
                "INT" to 1,
                "FT" to 2,
                "AET" to 2,
                "PEN" to 2,
                "PST" to 3,
                "CANC" to 3,
                "ABD" to 3,
                "AWD" to 3,
                "WO" to 3,
                "LIVE" to 1,
                "P" to 1
            )

            if (viewModel.listLoadedFootball[day] == null) {
                findNavController().navigate(R.id.noInternetFragment)
            } else {
                for (data in viewModel.listLoadedFootball[day]?.response!!) {
                    val status = data.fixture.status.short
                    val priority = abbreviationPriorities.entries.firstOrNull { entry ->
                        status == entry.key
                    }?.value ?: 0

                    prioritiesMap[data.fixture.id] = priority
                }

                val sortedList = viewModel.listLoadedFootball[day]!!.response.sortedBy{
                    prioritiesMap[it.fixture.id]
                }
                viewModel.currentDayMatches.addAll(sortedList)
                viewModel.currentPriorityMap = prioritiesMap
            }

        }
    }








    private fun recycler() {
        val layoutManager = LinearLayoutManager(requireContext())
        adapter = AdapterMatches(
            viewModel.currentDayMatches,
            viewModel.days,
            this,
            viewModel.currentPriorityMap,
            viewModel.currentDay,
            this,
            viewModel.placeholderSize1,
            viewModel.placeholderSize2
        )
        binding.recyclerMatches.adapter = adapter
        binding.recyclerMatches.layoutManager = layoutManager
        binding.recyclerMatches.setOnScrollChangeListener { _, _, _, _, _ ->
            if (layoutManager.findFirstVisibleItemPosition() > 0) binding.fab.show()
            else binding.fab.hide()
        }
    }

    override fun onAnotherDaySelected(day: Int) {
        collectMatchesForSelectedDay(day)
        viewModel.currentDay = day
        adapter.setNewList(viewModel.currentDayMatches, day, viewModel.currentPriorityMap)
    }

    override fun onMatchClicked(id: Int, type: Int) {
        viewModel.currentMatchId = id
        viewModel.currentType = type
        findNavController().navigate(R.id.matchDetailFragment)
    }


}