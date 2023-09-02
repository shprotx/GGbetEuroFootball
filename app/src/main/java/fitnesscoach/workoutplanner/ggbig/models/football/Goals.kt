package fitnesscoach.workoutplanner.ggbig.models.football

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Goals(
    val away: Int?,
    val home: Int?
)