package fitnesscoach.workoutplanner.ggbig.models.football

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Venue(
    val city: String?,
    val id: Int?,
    val name: String?
)