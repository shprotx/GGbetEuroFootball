package com.football.ggbeteurofootball.other

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService

object Checker {

    fun checkInternet(context: Context): Boolean {
        return try {
            val connectionManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val networkInfo = connectionManager.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected

        } catch (e: Exception) {
            false
        }
    }

}