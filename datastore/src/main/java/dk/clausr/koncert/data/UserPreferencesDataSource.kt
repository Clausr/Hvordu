package dk.clausr.koncert.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesDataSource @Inject constructor(
    @ApplicationContext context: Context,
) {
//    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

//    val userData = context.dataStore.data.map {
//
//    }

}