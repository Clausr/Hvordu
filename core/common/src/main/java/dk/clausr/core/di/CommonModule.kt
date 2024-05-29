package dk.clausr.core.di

import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class CommonModule {
    @Singleton
    @Provides
    fun provideWorkManager(
        @ApplicationContext context: Context,
        workerFactory: WorkerFactory,
    ): WorkManager {
        WorkManager.initialize(
            context,
            Configuration.Builder().run {
                setWorkerFactory(workerFactory)
                build()
            },
        )
        return WorkManager.getInstance(context)
    }
}