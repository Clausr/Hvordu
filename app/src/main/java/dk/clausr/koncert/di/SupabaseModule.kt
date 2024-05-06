//package dk.clausr.koncert.di
//
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import io.github.jan.supabase.SupabaseClient
//import io.github.jan.supabase.createSupabaseClient
//import io.github.jan.supabase.gotrue.Auth
//import io.github.jan.supabase.gotrue.auth
//import io.github.jan.supabase.postgrest.Postgrest
//import io.github.jan.supabase.postgrest.postgrest
//import io.github.jan.supabase.realtime.Realtime
//import io.github.jan.supabase.realtime.RealtimeChannel
//import io.github.jan.supabase.realtime.channel
//import io.github.jan.supabase.realtime.realtime
//import io.github.jan.supabase.storage.Storage
//import io.github.jan.supabase.storage.storage
//import javax.inject.Singleton
//
//@InstallIn(SingletonComponent::class)
//@Module
//object SupabaseModule {
//
//    @Singleton
//    @Provides
//    fun provideSupabase(): SupabaseClient = createSupabaseClient(
//        supabaseUrl = dk.clausr.koncert.BuildConfig.SUPABASE_URL,
//        supabaseKey = dk.clausr.koncert.BuildConfig.SUPABASE_ANON_KEY,
//    ) {
//        install(Postgrest)
//        install(Storage)
//        install(Realtime)
//    }
//
//    @Provides
//    @Singleton
//    fun provideSupabaseRealtime(client: SupabaseClient): Realtime {
//        return client.realtime
//    }
//
//    @Provides
//    @Singleton
//    fun provideSupabaseDatabase(client: SupabaseClient): Postgrest {
//        return client.postgrest
//    }
//
//    @Provides
//    @Singleton
//    fun provideSupabaseAuth(client: SupabaseClient): Auth {
//        return client.auth
//    }
//
//
//    @Provides
//    @Singleton
//    fun provideSupabaseStorage(client: SupabaseClient): Storage {
//        return client.storage
//    }
//
//    @Provides
//    @Singleton
//    fun provideSupabaseRealtimeChannel(realtime: Realtime): RealtimeChannel {
//        return realtime.channel("public")
//    }
//}