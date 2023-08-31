package com.football.ggbeteurofootball.api

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.football.ggbeteurofootball.models.datails.DetailMatch
import com.football.ggbeteurofootball.models.football.Football
import com.football.ggbeteurofootball.other.toDetailMatch
import com.football.ggbeteurofootball.other.toFootball



class FootballApiImplementation(
    private val context: Context
) {

    private val TAG = "FootballApiImplementation"
    private val BASE_URL = "https://v3.football.api-sports.io"

    suspend fun getFootballByDate(date: String, success: (football: Football?) -> Unit){
        val queue = Volley.newRequestQueue(context)
        val url = "$BASE_URL/fixtures?date=$date"


        val stringReq =
            object : JsonObjectRequest(
                Method.POST, url, null,
                Response.Listener { json ->
                    val results = json.get("results").toString().toInt()

                    if (json != null && results > 0)
                        success.invoke(json.toFootball())
                    else
                        success.invoke(null)

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

    suspend fun getDetailMatchByFixture(fixture: Int, success: (detailMatch: DetailMatch?) -> Unit){
        val queue = Volley.newRequestQueue(context)
        val url = "$BASE_URL/fixtures/statistics?fixture=$fixture"

        val stringReq =
            object : JsonObjectRequest(
                Method.POST, url, null,
                Response.Listener { json ->
                    val results = json.get("results").toString().toInt()

                    if (json != null && results > 0)
                        success.invoke(json.toDetailMatch())
                    else
                        success.invoke(null)
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