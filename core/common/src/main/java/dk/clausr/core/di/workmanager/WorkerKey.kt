package dk.clausr.core.di.workmanager

import androidx.annotation.Keep
import androidx.work.ListenableWorker
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Keep
annotation class WorkerKey(val value: KClass<out ListenableWorker>)
