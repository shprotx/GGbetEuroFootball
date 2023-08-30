package com.football.ggbeteurofootball.other

import com.football.ggbeteurofootball.models.Football
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.json.JSONObject

fun JSONObject.toFootball(): Football? {
    val moshi = Moshi.Builder().build()
    val adapter: JsonAdapter<Football> = moshi.adapter(Football::class.java)
    return adapter.fromJson(this.toString())
}