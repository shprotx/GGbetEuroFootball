package fitnesscoach.workoutplanner.ggbig.models.datails

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Response(
    val statistics: List<Statistic>,
    val team: Team
)