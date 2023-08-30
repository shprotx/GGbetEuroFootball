package com.football.ggbeteurofootball.adapters

import android.graphics.Color
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
import com.squareup.picasso.Picasso

class AdapterMatchWithoutStatistic(
    private var dataList: List<ItemH2H>,
    private val match: ItemMatch,
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
            holder.bind(match)
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

        }
    }





    inner class H2HHolder(b: ItemH2hBinding) : RecyclerView.ViewHolder(b.root) {


        fun bind(item: ItemH2H) {

        }
    }
}