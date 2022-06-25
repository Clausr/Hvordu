package dk.clausr.koncert.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dk.clausr.repo.concerts.ConcertRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ConcertRepositoryModule {
    @Singleton
    @Provides
    fun provideConcertRepository(@ApplicationContext context: Context): ConcertRepository = ConcertRepository(context)
}