package fitnesscoach.workoutplanner.ggbig.models.football

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Score(
    val extratime: Extratime,
    val fulltime: Fulltime,
    val halftime: Halftime,
    val penalty: Penalty
)