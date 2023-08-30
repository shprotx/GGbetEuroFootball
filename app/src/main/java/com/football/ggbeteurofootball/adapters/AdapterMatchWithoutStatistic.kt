package com.football.ggbeteurofootball.adapters

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.data.ItemDay
import com.football.ggbeteurofootball.data.ItemH2H
import com.football.ggbeteurofootball.data.ItemMatch
import com.football.ggbeteurofootball.databinding.ItemDatesBinding
import com.football.ggbeteurofootball.databinding.ItemDayBinding
import com.football.ggbeteurofootball.databinding.ItemH2hBinding
import com.football.ggbeteurofootball.databinding.ItemMatchBinding
import com.football.ggbeteurofootball.listeners.MatchesListListener
import com.football.ggbeteurofootball.models.Response
import com.squareup.picasso.Picasso
import java.util.Locale

class AdapterMatchWithoutStatistic(
    private var dataList: List<ItemH2H>,
    private val response: Response,
    private val type: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: ItemH2hBinding
    private lateinit var binding1: ItemMatchBinding

    companion object {
        private const val VIEW_TYPE_FIXED = 0
        private const val VIEW_TYPE_NORMAL = 1
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_FIXED -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_match, parent, false)
                binding1 = ItemMatchBinding.bind(view)
                FixedViewHolder(binding1)
            }
            VIEW_TYPE_NORMAL -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_h2h, parent, false)
                binding = ItemH2hBinding.bind(view)
                H2HHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }








    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is H2HHolder) {
            val dataItem = dataList[position - 1]
            holder.bind(dataItem)
        } else if (holder is FixedViewHolder)
            holder.bind(response)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_FIXED
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun getItemCount(): Int {
        return dataList.size + 1
    }







    inner class FixedViewHolder(b: ItemMatchBinding) : RecyclerView.ViewHolder(b.root) {
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

        fun bind(item: Response) {
            league.text = item.league.name

            if (item.teams.home.logo.isEmpty())
                firstTeamIcon.setImageResource(R.drawable.logo_placeholder)
            else
                Picasso.get().load(item.teams.home.logo).into(firstTeamIcon)

            if (item.teams.away.logo.isEmpty())
                secondTeamIcon.setImageResource(R.drawable.logo_placeholder__1_)
            else
                Picasso.get().load(item.teams.away.logo).into(secondTeamIcon)

            firstTeamName.text = item.teams.home.name
            secondTeamName.text = item.teams.away.name
            firstTeamScore.text = if (item.goals.home != null) "${item.goals.home}" else "-"
            secondTeamScore.text = if (item.goals.away != null) "${item.goals.away}" else "-"

            if (type != 3) {
                firstTeamStatus.setImageResource(R.drawable.status_green)
                secondTeamStatus.setImageResource(R.drawable.status_red)
            } else {
                firstTeamStatus.setImageResource(R.drawable.status_dark)
                secondTeamStatus.setImageResource(R.drawable.status_dark)
            }

            when (type) {
                1 -> {
                    status.text = "● Live"
                    status.setTextColor(Color.parseColor("#FE8001"))
                    light.isVisible = true
                }
                2 -> status.text = "Finished"
                3 -> status.text = getMatchTime(item.fixture.date)
            }

        }


        private fun getMatchTime(date: String): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val parsedDate = dateFormat.parse(date)
            val calendar = Calendar.getInstance().apply { time = parsedDate }
            return String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(
                Calendar.MINUTE))
        }
    }





    inner class H2HHolder(b: ItemH2hBinding) : RecyclerView.ViewHolder(b.root) {


        fun bind(item: ItemH2H) {

        }
    }
}