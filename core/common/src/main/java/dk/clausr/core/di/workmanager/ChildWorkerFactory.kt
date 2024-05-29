package dk.clausr.core.di.workmanager

import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

interface ChildWorkerFactory<T : ListenableWorker> {
    fun create(workerParams: WorkerParameters): T
}
