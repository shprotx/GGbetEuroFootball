package com.football.ggbeteurofootball.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.data.ItemDay
import com.football.ggbeteurofootball.data.ItemMatch
import com.football.ggbeteurofootball.databinding.ItemDatesBinding
import com.football.ggbeteurofootball.databinding.ItemDayBinding
import com.football.ggbeteurofootball.databinding.ItemMatchBinding
import com.football.ggbeteurofootball.listeners.MatchesListListener
import com.squareup.picasso.Picasso

class AdapterMatches(
    private var dataList: List<ItemMatch>,
    private val days: List<ItemDay>,
    private val listener: MatchesListListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: ItemDatesBinding
    private lateinit var binding1: ItemMatchBinding
    private var selectedDay = 3

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
            val dataItem = dataList[position - 1]
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
        return dataList.size + 1
    }

    fun setNewList(list: List<ItemMatch>, day: Int) {
        dataList = list
        selectedDay = day
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
                        listener.onAnotherDaySelected(index)
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

        fun bind(item: ItemMatch) {
            league.text = item.league

            if (item.firstTeamIcon.isEmpty())
                firstTeamIcon.setImageResource(R.drawable.logo_placeholder)
            else
                Picasso.get().load(item.firstTeamIcon).into(firstTeamIcon)

            if (item.secondTeamIcon.isEmpty())
                secondTeamIcon.setImageResource(R.drawable.logo_placeholder__1_)
            else
                Picasso.get().load(item.secondTeamIcon).into(secondTeamIcon)

            firstTeamName.text = item.firstTeamName
            secondTeamName.text = item.secondTeamName
            firstTeamScore.text = if (item.type != 3) "${item.firstTeamScore}" else "-"
            secondTeamScore.text = if (item.type != 3) "${item.secondTeamScore}" else "-"

            if (item.type != 3) {
                firstTeamStatus.setImageResource(R.drawable.status_green)
                secondTeamStatus.setImageResource(R.drawable.status_red)
            } else {
                firstTeamStatus.setImageResource(R.drawable.status_dark)
                secondTeamStatus.setImageResource(R.drawable.status_dark)
            }

            when (item.type) {
                1 -> {
                    status.text = "● Live"
                    status.setTextColor(Color.parseColor("#FE8001"))
                    light.isVisible = true
                }
                2 -> status.text = "Finished"
                3 -> status.text = "16:30"
            }

            matchCard.setOnClickListener {
                listener.onMatchClicked(item.id, adapterPosition)
            }
        }
    }
}