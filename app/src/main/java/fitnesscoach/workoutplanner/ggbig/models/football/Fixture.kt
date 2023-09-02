package fitnesscoach.workoutplanner.ggbig.models.football

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Fixture(
    val date: String,
    val id: Int,
    val periods: Periods,
    val referee: String?,
    val status: Status,
    val timestamp: Int,
    val timezone: String,
    val venue: Venue
)