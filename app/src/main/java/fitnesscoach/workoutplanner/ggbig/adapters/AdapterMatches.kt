package fitnesscoach.workoutplanner.ggbig.adapters

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fitnesscoach.workoutplanner.ggbig.R
import fitnesscoach.workoutplanner.ggbig.data.ItemDay
import fitnesscoach.workoutplanner.ggbig.databinding.ItemDatesBinding
import fitnesscoach.workoutplanner.ggbig.databinding.ItemDayBinding
import fitnesscoach.workoutplanner.ggbig.databinding.ItemMatchBinding
import fitnesscoach.workoutplanner.ggbig.listeners.DaySelectedListener
import fitnesscoach.workoutplanner.ggbig.listeners.MatchesSelectedListener
import fitnesscoach.workoutplanner.ggbig.models.football.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.Locale

class AdapterMatches(
    private var response: MutableList<Response>,
    private val days: List<ItemDay>,
    private val listener: MatchesSelectedListener,
    private var priorityMap: MutableMap<Int, Int>,
    private var selectedDay: Int,
    private var listenerDay: DaySelectedListener,
    private val size1: Int,
    private val size2: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: ItemDatesBinding
    private lateinit var binding1: ItemMatchBinding

    companion object {
        private const val VIEW_TYPE_FIXED = 0
        private const val VIEW_TYPE_NORMAL = 1
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_FIXED -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_dates, parent, false)
                binding = ItemDatesBinding.bind(view)
                FixedViewHolder(binding)
            }
            VIEW_TYPE_NORMAL -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_match, parent, false)
                binding1 = ItemMatchBinding.bind(view)
                MatchViewHolder(binding1)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }








    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MatchViewHolder) {
            val dataItem = response[position - 1]
            holder.bind(dataItem)
        } else if (holder is FixedViewHolder)
            holder.bind()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_FIXED
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun getItemCount(): Int {
        return response.size + 1
    }

    fun setNewList(list: MutableList<Response>, day: Int, map: MutableMap<Int, Int>) {
        response = list
        selectedDay = day
        priorityMap = map
        notifyDataSetChanged()
    }






    inner class FixedViewHolder(b: ItemDatesBinding) : RecyclerView.ViewHolder(b.root) {
        private val list = listOf(b.d1, b.d2, b.d3, b.d4, b.d5, b.d6, b.d7)
        fun bind() {
            for (i in list.indices) {
                list[i].dayName.text = days[i].name
                list[i].dayNumber.text = days[i].number.toString()
                if (i == selectedDay) {
                    list[i].dayBack.setImageResource(R.drawable.background_day_selected)
                    list[i].dayName.setTextColor(Color.parseColor("#FE8001"))
                    list[i].dayNumber.setTextColor(Color.parseColor("#FE8001"))
                } else {
                    list[i].dayBack.setImageResource(R.drawable.background_day_unselected)
                    list[i].dayName.setTextColor(Color.parseColor("#757A87"))
                    list[i].dayNumber.setTextColor(Color.parseColor("#757A87"))
                }
                setButtonOnClick(list[i].dayBack, list, i)
            }
        }

        fun setButtonOnClick(button: ImageView, buttons: List<ItemDayBinding>, index: Int) {
            button.setOnClickListener {
                for (b in buttons) {
                    if (b.dayBack == button) {
                        b.dayBack.setImageResource(R.drawable.background_day_selected)
                        b.dayName.setTextColor(Color.parseColor("#FE8001"))
                        b.dayNumber.setTextColor(Color.parseColor("#FE8001"))
                        selectedDay = index
                        listenerDay.onAnotherDaySelected(index)
                    } else {
                        b.dayBack.setImageResource(R.drawable.background_day_unselected)
                        b.dayName.setTextColor(Color.parseColor("#757A87"))
                        b.dayNumber.setTextColor(Color.parseColor("#757A87"))
                    }
                }
            }
        }
    }





    inner class MatchViewHolder(b: ItemMatchBinding) : RecyclerView.ViewHolder(b.root) {
        private val league = b.leagueName
        private val status = b.status
        private val firstTeamIcon = b.firstTeamIcon
        private val firstTeamName = b.firstTeamName
        private val firstTeamScore = b.firstTeamScore
        private val firstTeamStatus = b.firstTeamStatus
        private val secondTeamIcon = b.secondTeamIcon
        private val secondTeamName = b.secondTeamName
        private val secondTeamScore = b.secondTeamScore
        private val secondTeamStatus = b.secondTeamStatus
        private val light = b.light
        private val matchCard = b.matchCard

        fun bind(item: Response) {
            league.text = item.league.name

            setPicture(item.teams.home.logo, firstTeamIcon)
            setPicture(item.teams.away.logo, secondTeamIcon)

            firstTeamName.text = item.teams.home.name
            secondTeamName.text = item.teams.away.name
            firstTeamScore.text = if (item.goals.home != null) "${item.goals.home}" else "-"
            secondTeamScore.text = if (item.goals.away != null) "${item.goals.away}" else "-"

            if (item.goals.home != null && item.goals.away != null) {
                if (item.goals.home > item.goals.away) {
                    firstTeamStatus.setImageResource(R.drawable.status_green)
                    secondTeamStatus.setImageResource(R.drawable.status_red)
                } else if (item.goals.home < item.goals.away) {
                    firstTeamStatus.setImageResource(R.drawable.status_red)
                    secondTeamStatus.setImageResource(R.drawable.status_green)
                } else {
                    firstTeamStatus.setImageResource(R.drawable.status_green)
                    secondTeamStatus.setImageResource(R.drawable.status_green)
                }
            } else {
                firstTeamStatus.setImageResource(R.drawable.status_dark)
                secondTeamStatus.setImageResource(R.drawable.status_dark)
            }

            light.isVisible = false
            status.setTextColor(Color.parseColor("#757A87"))

            when (priorityMap[item.fixture.id]) {
                1 -> {
                    status.text = "● Live"
                    status.setTextColor(Color.parseColor("#FE8001"))
                    light.isVisible = true
                }
                2 -> status.text = "Finished"
                3 -> status.text = getMatchTime(item.fixture.date)
                4 -> status.text = "Postponed"
                5 -> status.text = "Cancelled"
            }

            matchCard.setOnClickListener {
                listener.onMatchClicked(item.fixture.id, priorityMap[item.fixture.id]!!)
            }
        }


        private fun getMatchTime(date: String): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val parsedDate = dateFormat.parse(date)
            val calendar = Calendar.getInstance().apply { time = parsedDate }
            return String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
        }


        private fun setPicture(img: String, target: ImageView) {
            CoroutineScope(Dispatchers.IO).launch {
                val imgSize = withContext(Dispatchers.IO) {
                    URL(img).openConnection()
                }.contentLength
                if (!(imgSize == size1 || imgSize == size2))
                    withContext(Dispatchers.Main) { Picasso.get().load(img).into(target) }
            }
        }
    }
}