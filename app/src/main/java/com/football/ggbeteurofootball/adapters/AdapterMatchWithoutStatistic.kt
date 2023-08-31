package com.football.ggbeteurofootball.adapters

import android.content.Context
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.football.ggbeteurofootball.R
import com.football.ggbeteurofootball.data.ItemH2H
import com.football.ggbeteurofootball.databinding.ItemH2hBinding
import com.football.ggbeteurofootball.databinding.ItemMatchBinding
import com.football.ggbeteurofootball.databinding.ItemTitleBinding
import com.football.ggbeteurofootball.models.football.Response
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.Locale

class AdapterMatchWithoutStatistic(
    private val context: Context,
    private var dataList: List<ItemH2H>,
    private val response: Response,
    private val type: Int,
    private val size1: Int,
    private val size2: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: ItemH2hBinding
    private lateinit var binding1: ItemMatchBinding
    private lateinit var binding2: ItemTitleBinding
    private val TAG = "AdapterMatchWithoutStatistic"

    companion object {
        private const val VIEW_TYPE_FIXED = 0
        private const val VIEW_TYPE_NORMAL = 1
        private const val VIEW_TYPE_TITLE = 2
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_FIXED -> {
                binding1 = ItemMatchBinding.bind(inflater.inflate(R.layout.item_match, parent, false))
                FixedViewHolder(binding1)
            }
            VIEW_TYPE_NORMAL -> {
                binding = ItemH2hBinding.bind(inflater.inflate(R.layout.item_h2h, parent, false))
                H2HHolder(binding)
            }
            VIEW_TYPE_TITLE -> {
                binding2 = ItemTitleBinding.bind(inflater.inflate(R.layout.item_title, parent, false))
                TitleHolder(binding2)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }








    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is H2HHolder -> {
                val dataItem = dataList[position - 2]
                holder.bind(dataItem)
            }
            is FixedViewHolder -> holder.bind(response)
            is TitleHolder -> holder.bind(dataList.size)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 ->  VIEW_TYPE_FIXED
            1 ->  VIEW_TYPE_TITLE
            else -> VIEW_TYPE_NORMAL
        }
    }


    override fun getItemCount(): Int {
        return dataList.size + 2
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

            when (type) {
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





    inner class H2HHolder(b: ItemH2hBinding) : RecyclerView.ViewHolder(b.root) {
        private val homeTeamIcon = b.firstTeamIcon
        private val awayTeamIcon = b.secondTeamIcon
        private val homeTeamScore = b.firstTeamScore
        private val awayTeamScore = b.secondTeamScore
        private val date = b.dateText
        private val homeTeamIndicator = b.firstTeamIndicator
        private val awayTeamIndicator = b.secondTeamIndicator

        fun bind(item: ItemH2H) {
            Picasso.get().load(item.homeTeamIcon).into(homeTeamIcon)
            Picasso.get().load(item.awayTeamIcon).into(awayTeamIcon)
            homeTeamScore.text = item.homeTeamScore.toString()
            awayTeamScore.text = item.awayTeamScore.toString()
            date.text = item.date
            if (item.homeTeamScore > item.awayTeamScore) {
                homeTeamIndicator.setImageResource(R.drawable.status_green)
                awayTeamIndicator.setImageResource(R.drawable.status_red)
            } else if (item.homeTeamScore < item.awayTeamScore) {
                homeTeamIndicator.setImageResource(R.drawable.status_red)
                awayTeamIndicator.setImageResource(R.drawable.status_green)
            } else {
                homeTeamIndicator.setImageResource(R.drawable.status_green)
                awayTeamIndicator.setImageResource(R.drawable.status_green)
            }
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
                title.text = "H2H"
                title.setTextColor(Color.WHITE)
                title.textSize = 18f
                title.typeface = ResourcesCompat.getFont(context, R.font.roboto_600)
            }
        }
    }
}