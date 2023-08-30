package com.football.ggbeteurofootball.di

import android.content.Context
import com.football.ggbeteurofootball.api.FootballApiImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {

    @Singleton
    @Provides
    fun provideFootballApiImplementation(@ApplicationContext context: Context): FootballApiImplementation {
        return FootballApiImplementation(context)
    }
}