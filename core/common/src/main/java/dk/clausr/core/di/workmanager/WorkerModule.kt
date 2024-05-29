package dk.clausr.core.di.workmanager

import androidx.work.WorkerFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface WorkerModule {

    @Binds
    fun bindWorkerFactory(factory: DaggerWorkerFactory): WorkerFactory
}