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
        UserData(
            username = it.userName,
            group = it.group,
            keyboardHeight = if (it.hasKeyboardHeight()) it.keyboardHeight else null
        )
    }

    suspend fun setUserPreferences(userData: UserData) {
        userPreferenceDataStore.updateData { currentData ->
            currentData.toBuilder()
                .setUserName(userData.username)
                .setGroup(userData.group)
                .build()
        }
    }

    suspend fun setKeyboardHeight(keyboardHeight: Float) {
        userPreferenceDataStore.updateData { currentData ->
            currentData.toBuilder()
                .setKeyboardHeight(keyboardHeight)
                .build()
        }
    }
}