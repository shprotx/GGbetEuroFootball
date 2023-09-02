package fitnesscoach.workoutplanner.ggbig.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fitnesscoach.workoutplanner.ggbig.api.FootballApiImplementation
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {

    @Singleton
    @Provides
    fun provideFootballApiImplementation(@ApplicationContext context: Context): FootballApiImplementation {
        return FootballApiImplementation(context)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("GGbetPreferences", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideEditor(@ApplicationContext context: Context): SharedPreferences.Editor {
        return provideSharedPreferences(context).edit()
    }
}