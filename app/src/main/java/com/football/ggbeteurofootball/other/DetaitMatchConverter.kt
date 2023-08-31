package com.football.ggbeteurofootball.other

import com.football.ggbeteurofootball.models.datails.DetailMatch
import com.football.ggbeteurofootball.models.football.Football
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.json.JSONObject

fun JSONObject.toDetailMatch(): DetailMatch? {
    val moshi = Moshi.Builder().build()
    val adapter: JsonAdapter<DetailMatch> = moshi.adapter(DetailMatch::class.java)
    return adapter.fromJson(this.toString())
}