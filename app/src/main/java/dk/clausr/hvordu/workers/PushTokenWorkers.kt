package dk.clausr.hvordu.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.google.firebase.messaging.FirebaseMessaging
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dk.clausr.core.di.workmanager.ChildWorkerFactory
import dk.clausr.hvordu.repo.userdata.UserRepository
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.IOException

const val PUSH_TOKEN_KEY = "dk.clausr.hvordu.android.push.PUSH_TOKEN_KEY"


class PushTokenUploadWorker @AssistedInject constructor(
    context: Context,
    @Assisted workerParams: WorkerParameters,
    private val userRepository: UserRepository,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val newPushToken = inputData.getString(PUSH_TOKEN_KEY) ?: return Result.failure()
            Timber.d("Upload push token: $newPushToken")

            val newPushTokenUploadResponse = userRepository.setFcmToken(newPushToken)
            Timber.d("Upload push token api response: $newPushTokenUploadResponse")

            return Result.success()
        } catch (ioException: IOException) {
            return Result.retry()
        } catch (exception: Exception) {
            return Result.failure()
        }
    }

    @AssistedFactory
    interface Factory : ChildWorkerFactory<PushTokenUploadWorker>
}


class PushTokenResolverWorker @AssistedInject constructor(
    context: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
                ?: return Result.retry()

            val outputData = Data.Builder()
                .putString(PUSH_TOKEN_KEY, token)
                .build()

            Timber.d("FirebaseMessaging: $token")
            return Result.success(outputData)
        } catch (exception: IOException) {
            Timber.wtf(exception)
            return Result.retry()
        } catch (exception: Exception) {
            return Result.failure()
        }
    }

    @AssistedFactory
    interface Factory : ChildWorkerFactory<PushTokenUploadWorker>
}