package dk.clausr.repo.userdata

import dk.clausr.core.models.UserData
import dk.clausr.koncert.data.UserPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userSettingDataSource: UserPreferencesDataSource,
) {
    fun getUserData(): Flow<UserData?> = userSettingDataSource.userData.map {
        if (it.username.isBlank() && it.group.isBlank()) {
            null
        } else {
            it
        }
    }

    suspend fun setUserData(userData: UserData) {
        userSettingDataSource.setUserPreferences(userData)
    }

    suspend fun setKeyboardHeight(height: Float) {
        userSettingDataSource.setKeyboardHeight(height)
    }

}