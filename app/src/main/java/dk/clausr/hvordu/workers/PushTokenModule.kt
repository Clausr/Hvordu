package dk.clausr.hvordu.workers

import androidx.work.ListenableWorker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dk.clausr.core.di.workmanager.ChildWorkerFactory
import dk.clausr.core.di.workmanager.WorkerKey


@InstallIn(SingletonComponent::class)
@Module
interface PushTokenModule {

    @Binds
    @IntoMap
    @WorkerKey(PushTokenUploadWorker::class)
    fun bindPushTokenUploadWorker(factory: PushTokenUploadWorker.Factory): ChildWorkerFactory<out ListenableWorker>

    @Binds
    @IntoMap
    @WorkerKey(PushTokenResolverWorker::class)
    fun bindPushTokenResolverWorker(factory: PushTokenResolverWorker.Factory): ChildWorkerFactory<out ListenableWorker>

}