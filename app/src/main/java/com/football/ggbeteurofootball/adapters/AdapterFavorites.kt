package com.football.ggbeteurofootball.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.databinding.ItemButtonsBinding
import com.football.ggbeteurofootball.databinding.ItemMatchBinding
import com.football.ggbeteurofootball.listeners.FavoriteMatchSortListener
import com.football.ggbeteurofootball.listeners.MatchesSelectedListener
import com.football.ggbeteurofootball.models.Response
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.Locale

class AdapterFavorites(
    private var response: MutableList<Response>,
    private var priorityMap: MutableMap<Int, Int>,
    private val listener: FavoriteMatchSortListener,
    private val listenerSelect: MatchesSelectedListener,
    private val size1: Int,
    private val size2: Int
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding1: ItemMatchBinding
    private lateinit var binding: ItemButtonsBinding

    companion object {
        private const val VIEW_TYPE_FIXED = 0
        private const val VIEW_TYPE_NORMAL = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_FIXED -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_buttons, parent, false)
                binding = ItemButtonsBinding.bind(view)
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

    override fun getItemCount(): Int {
        return response.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MatchViewHolder) {
            val dataItem = response[position - 1]
            holder.bind(dataItem)
        } else if (holder is FixedViewHolder)
            holder.bind()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_FIXED
        else VIEW_TYPE_NORMAL
    }

    fun setNewSortedList(list: List<Response>) {
        response = list.toMutableList()
        notifyDataSetChanged()
    }






    inner class FixedViewHolder(b: ItemButtonsBinding) : RecyclerView.ViewHolder(b.root) {
        private val list = listOf(b.upcoming, b.Live, b.finished)
        fun bind() {
            for (i in list.indices) {
                setButtonOnClick(list[i], list, i)
            }
        }

        private fun setButtonOnClick(button: MaterialButton, buttons: List<MaterialButton>, index: Int) {
            button.setOnClickListener {
                for (b in buttons) {
                    if (b == button) {
                        b.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FE8001"))
                        b.setIconResource(R.drawable.icon_ok)
                        b.setTextColor(Color.WHITE)
                        listener.onSortButtonPressed(index)
                    } else {
                        b.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#212632"))
                        b.setTextColor(Color.parseColor("#757A87"))
                        b.icon = null
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
                listenerSelect.onMatchClicked(item.fixture.id, priorityMap[item.fixture.id]!!)
            }
        }


        private fun getMatchTime(date: String): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val parsedDate = dateFormat.parse(date)
            val calendar = Calendar.getInstance().apply { time = parsedDate }
            return String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(
                Calendar.MINUTE))
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