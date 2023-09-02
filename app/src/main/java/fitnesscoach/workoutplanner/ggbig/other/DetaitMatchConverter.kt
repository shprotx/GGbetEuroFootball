package fitnesscoach.workoutplanner.ggbig.other

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import fitnesscoach.workoutplanner.ggbig.models.datails.DetailMatch
import org.json.JSONObject

fun JSONObject.toDetailMatch(): DetailMatch? {
    val moshi = Moshi.Builder().build()
    val adapter: JsonAdapter<DetailMatch> = moshi.adapter(DetailMatch::class.java)
    return adapter.fromJson(this.toString())
}