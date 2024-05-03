package dk.clausr.koncert.data

import androidx.datastore.core.DataStore
import dk.clausr.core.models.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesDataSource @Inject constructor(
    private val userPreferenceDataStore: DataStore<UserPreferences>,
) {
    val userData = userPreferenceDataStore.data.map {
        UserData(it.userName, it.group)
    }

    suspend fun setUserPreferences(userData: UserData) {
        userPreferenceDataStore.updateData { currentData ->
            currentData.toBuilder()
                .setUserName(userData.username)
                .setGroup(userData.group)
                .build()
        }
    }
}