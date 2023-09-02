package fitnesscoach.workoutplanner.ggbig.models.datails

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class Parameters(
    val fixture: String
)