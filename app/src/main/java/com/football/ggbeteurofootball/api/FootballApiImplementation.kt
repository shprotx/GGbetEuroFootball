package com.football.ggbeteurofootball.api

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.football.ggbeteurofootball.models.Football
import com.football.ggbeteurofootball.other.toFootball



class FootballApiImplementation(
    private val context: Context
) {

    suspend fun getFootballByDate(date: String, success: (football: Football?) -> Unit){
        val queue = Volley.newRequestQueue(context)
        val url = "https://v3.football.api-sports.io/fixtures?date=$date"


        val stringReq =
            object : JsonObjectRequest(
                Method.POST, url, null,
                Response.Listener { json ->
                    Log.i("mylog", "teatModelOne = ${json.toFootball()}")

                    success.invoke(json.toFootball())
                },
                Response.ErrorListener { error ->
                    Log.i("mylog", "error = " + error)
                    success.invoke(null)

                }
            ) {

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["x-rapidapi-key"] = "6b5e993ad23a4d155b992de4e5db82a6"
                    return headers
                }

            }
        queue.add(stringReq)
    }
}