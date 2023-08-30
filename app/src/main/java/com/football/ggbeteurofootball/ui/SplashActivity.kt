package com.football.ggbeteurofootball.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.football.ggbeteurofootball.api.FootballApiImplementation
import com.football.ggbeteurofootball.data.ItemDay
import com.football.ggbeteurofootball.databinding.ActivitySplashBinding
import com.football.ggbeteurofootball.ui.MainViewModel.listLoadedFootball
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject lateinit var footballApiImplementation: FootballApiImplementation
    private lateinit var binding: ActivitySplashBinding
    private val viewModel = MainViewModel
    private var isAllDataCollected = false
    private var TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.progress = 0
        binding.progressBar.max = 100

        lifecycleScope.launch {
            for (i in 0..100) {
                binding.progressBar.progress = i
                if (!isAllDataCollected) delay(1000)
                else delay(10)
            }
            viewModel.currentDay = 3
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }

        determineDates()
        getFootballData()

    }






    private fun getFootballData() {
        Log.d(TAG, "getFootballData viewModel.days: ${viewModel.days}")

        var countLoadedFootball = 0

        viewModel.days.forEachIndexed { index, itemDay ->
            Log.d(TAG, "getFootballData:  outside coroutine ${itemDay.date}")
            CoroutineScope(Dispatchers.IO).launch{
                footballApiImplementation.getFootballByDate(itemDay.date){
                    Log.d(TAG, "getFootballData: ${itemDay.date}")

                    viewModel.listLoadedFootball.add(index, it)
                    countLoadedFootball++
                    //AllData loaded
                    if (countLoadedFootball == 7)
                        isAllDataCollected = true

                }
            }
        }
    }








    private fun determineDates() {
        if (viewModel.days.isEmpty()) {
            val currentDate = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dayOfWeekFormat = SimpleDateFormat("EEE", Locale.getDefault())

            for (i in 3 downTo 1) {
                val date = Calendar.getInstance()
                date.add(Calendar.DAY_OF_YEAR, -i)
                val dateString = dateFormat.format(date.time)
                val dayOfWeek = dayOfWeekFormat.format(date.time)
                val dayOfMonth = date.get(Calendar.DAY_OF_MONTH)
                viewModel.days.add(ItemDay(dateString, dayOfWeek, dayOfMonth))
            }

            val currentDateStr = dateFormat.format(currentDate.time)
            val currentDayOfWeek = dayOfWeekFormat.format(currentDate.time)
            val currentDayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)
            viewModel.days.add(ItemDay(currentDateStr, currentDayOfWeek, currentDayOfMonth))

            for (i in 1..3) {
                val date = Calendar.getInstance()
                date.add(Calendar.DAY_OF_YEAR, i)
                val dateString = dateFormat.format(date.time)
                val dayOfWeek = dayOfWeekFormat.format(date.time)
                val dayOfMonth = date.get(Calendar.DAY_OF_MONTH)
                viewModel.days.add(ItemDay(dateString, dayOfWeek, dayOfMonth))
            }
        }

    }
}