package com.football.ggbeteurofootball.adapters

import android.content.Context
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.data.ItemH2H
import com.football.ggbeteurofootball.data.ItemStatisticHeader
import com.football.ggbeteurofootball.databinding.ItemH2hBinding
import com.football.ggbeteurofootball.databinding.ItemMatchBinding
import com.football.ggbeteurofootball.databinding.ItemStatisticBodyBinding
import com.football.ggbeteurofootball.databinding.ItemStatisticHeaderBinding
import com.football.ggbeteurofootball.databinding.ItemTitleBinding
import com.football.ggbeteurofootball.models.Response
import com.squareup.picasso.Picasso
import java.util.Locale

class AdapterMatchWithStatistic(
    private val context: Context,
    private var dataList: List<ItemH2H>,
    private val response: Response,
    private val header: ItemStatisticHeader
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: ItemStatisticBodyBinding
    private lateinit var binding1: ItemMatchBinding
    private lateinit var binding2: ItemTitleBinding
    private lateinit var binding3: ItemStatisticHeaderBinding

    companion object {
        private const val VIEW_TYPE_FIXED = 0
        private const val VIEW_TYPE_NORMAL = 1
        private const val VIEW_TYPE_TITLE = 2
        private const val VIEW_TYPE_HEADER = 3
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_FIXED -> {
                binding1 = ItemMatchBinding.bind(inflater.inflate(R.layout.item_match, parent, false))
                FixedViewHolder(binding1)
            }
            VIEW_TYPE_NORMAL -> {
                binding = ItemStatisticBodyBinding.bind(inflater.inflate(R.layout.item_statistic_body, parent, false))
                StatHolder(binding)
            }
            VIEW_TYPE_TITLE -> {
                binding2 = ItemTitleBinding.bind(inflater.inflate(R.layout.item_title, parent, false))
                TitleHolder(binding2)
            }
            VIEW_TYPE_HEADER -> {
                binding3 = ItemStatisticHeaderBinding
                    .bind(inflater.inflate(R.layout.item_statistic_header, parent, false))
                HeaderHolder(binding3)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }








    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StatHolder -> {
                val dataItem = dataList[position - 2]
                holder.bind(dataItem)
            }
            is FixedViewHolder -> holder.bind(response)
            is TitleHolder -> holder.bind(dataList.size)
            is HeaderHolder -> holder.bind(header)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 ->  VIEW_TYPE_FIXED
            1 ->  VIEW_TYPE_TITLE
            2 -> VIEW_TYPE_HEADER
            else -> VIEW_TYPE_NORMAL
        }
    }


    override fun getItemCount(): Int {
        return dataList.size + 3
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

            status.text = "Finished"

        }
    }





    inner class StatHolder(b: ItemStatisticBodyBinding) : RecyclerView.ViewHolder(b.root) {
        private val title = b.title
        private val homeNumber = b.homeValue
        private val homeProgress = b.homeProgress
        private val awayNumber = b.awayValue
        private val awayProgress = b.awayProgress

        fun bind(item: ItemH2H) {

        }
    }





    inner class TitleHolder(b: ItemTitleBinding): RecyclerView.ViewHolder(b.root) {
        private val title = b.title

        fun bind(size: Int) {
            if (size == 0) {
                title.text = "No information about this match was found. Please, choose another match or come back later!"
                title.setTextColor(Color.parseColor("#757A87"))
                title.textSize = 14f
                title.typeface = ResourcesCompat.getFont(context, R.font.roboto_400)
            } else {
                title.text = "Statistic"
                title.setTextColor(Color.WHITE)
                title.textSize = 18f
                title.typeface = ResourcesCompat.getFont(context, R.font.roboto_600)
            }
        }
    }






    inner class HeaderHolder(b: ItemStatisticHeaderBinding): RecyclerView.ViewHolder(b.root) {
        private val homeTeamIcon = b.homeStatIcon
        private val awayTeamIcon = b.awayStatIcon
        private val homeTeamProgress = b.homeProgress
        private val awayTeamProgress = b.awayProgress
        private val homeProgressPercent = b.homeProgressValue
        private val awayProgressPercent = b.awayProgressValue
        fun bind(item: ItemStatisticHeader) {
            homeProgressPercent.text = "${item.homeProgressValue}%"
            awayProgressPercent.text = "${item.awayProgressValue}%"
            homeTeamProgress.progress = item.homeProgressValue
            awayTeamProgress.progress = item.awayProgressValue
            Picasso.get().load(response.teams.home.logo).into(homeTeamIcon)
            Picasso.get().load(response.teams.away.logo).into(awayTeamIcon)
        }
    }
}