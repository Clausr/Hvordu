package dk.clausr.core.di.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider


class DaggerWorkerFactory @Inject constructor(
    private val workerFactories: Map<Class<out ListenableWorker>, @JvmSuppressWildcards Provider<ChildWorkerFactory<out ListenableWorker>>>,
) : WorkerFactory() {
    @Suppress("SwallowedException")
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return try {
            val clazz = Class.forName(workerClassName)
            val foundEntry = workerFactories.entries.find { clazz.isAssignableFrom(it.key) }
            val factory = foundEntry?.value
            if (factory == null) {
                Timber.wtf("Unknown worker class name: $workerClassName")
                null
            } else {
                factory.get().create(workerParameters)
            }
        } catch (e: ClassNotFoundException) {
            Timber.wtf("Missing worker class name: $workerClassName")
            null
        }
    }
}
