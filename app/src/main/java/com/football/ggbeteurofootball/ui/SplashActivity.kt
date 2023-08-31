package com.football.ggbeteurofootball.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.football.ggbeteurofootball.api.FootballApiImplementation
import com.football.ggbeteurofootball.data.ItemDay
import com.football.ggbeteurofootball.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.Locale
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject lateinit var footballApiImplementation: FootballApiImplementation
    @Inject lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivitySplashBinding
    private val viewModel = MainViewModel
    private var isAllDataCollected = false
    private var TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeOrientation()
        hideNavigationBar()
        initProgressBar()
        getDataFromInternet()
        determinePlaceholders()
        getDataFromPrefs()
        determineDates()
        getFootballData()
        listeners()

    }






    private fun hideNavigationBar() {
        val flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = flags
    }






    private fun initProgressBar() {
        binding.progressBar.progress = 0
        binding.progressBar.max = 100
    }






    private fun getDataFromInternet() {
        lifecycleScope.launch {
            for (i in 0..100) {
                binding.progressBar.progress = i
                if (!isAllDataCollected) delay(1000)
                else delay(10)
            }
            viewModel.currentDay = 3

            if (checkInternet())
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            else {
                binding.noInternetText.isVisible = true
                binding.swipeLayout.isRefreshing = false
                isAllDataCollected = false
            }

        }
    }







    private fun checkInternet(): Boolean {
        return try {
            val connectionManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val networkInfo = connectionManager.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected

        } catch (e: Exception) {
            false
        }
    }
    
    

    
    
    
    
    private fun listeners() {
        binding.swipeLayout.setOnRefreshListener {
            binding.noInternetText.isVisible = false
            determinePlaceholders()
            getFootballData()
            getDataFromInternet()
        }
    }






    private fun getDataFromPrefs() {
        val list = sharedPreferences.getString("favorite", "")
        if (!list.isNullOrEmpty())
            viewModel.favoriteMatches = stringToIntList(list)
    }





    private fun stringToIntList(string: String): MutableList<Int> {
        return string.split(",").map { it.toInt() }.toMutableList()
    }






    private fun getFootballData() {
        var countLoadedFootball = 0

        viewModel.days.forEachIndexed { index, itemDay ->
            CoroutineScope(Dispatchers.IO).launch{
                footballApiImplementation.getFootballByDate(itemDay.date){

                    viewModel.listLoadedFootball[index] = it
                    countLoadedFootball++
                    //AllData loaded
                    if (countLoadedFootball == 7)
                        isAllDataCollected = true

                }
            }
        }
    }







    private fun determinePlaceholders() {
        CoroutineScope(Dispatchers.IO).launch {
            val url = "https://media-3.api-sports.io/football/teams/15176.png"
            val url1 = "https://media-3.api-sports.io/football/teams/2642.png"
            val connection = withContext(Dispatchers.IO) {
                URL(url).openConnection()
            }
            val connection1 = withContext(Dispatchers.IO) {
                URL(url1).openConnection()
            }
            viewModel.placeholderSize1 = connection.contentLength
            viewModel.placeholderSize2 = connection1.contentLength
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







    private fun changeOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
    }
}