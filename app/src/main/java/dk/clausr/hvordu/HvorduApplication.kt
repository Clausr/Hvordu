package dk.clausr.hvordu

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class HvorduApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        setupLogging()
    }

    private fun setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache(MemoryCache.Builder(this).maxSizePercent(0.25).build())
            .diskCache {
                DiskCache
                    .Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(100 * 1024 * 1024)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .crossfade(true)
            .logger(if (BuildConfig.DEBUG) DebugLogger() else null)
            .build()
    }
}