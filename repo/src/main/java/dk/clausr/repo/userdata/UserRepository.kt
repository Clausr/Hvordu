package dk.clausr.repo.userdata

import dk.clausr.core.models.UserData
import dk.clausr.koncert.data.UserPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userSettingDataSource: UserPreferencesDataSource,
) {
    fun getUserData(): Flow<UserData?> = userSettingDataSource.userData

    suspend fun setUserData(userData: UserData) {
        userSettingDataSource.setUserPreferences(userData)
    }

}