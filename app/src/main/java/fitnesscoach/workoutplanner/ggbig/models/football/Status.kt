package fitnesscoach.workoutplanner.ggbig.models.football

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Status(
    val elapsed: Int?,
    val long: String,
    val short: String
)