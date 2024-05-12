package dk.clausr.repo.userdata

import dk.clausr.core.models.UserData
import dk.clausr.koncert.api.GroupsApi
import dk.clausr.koncert.api.ProfileApi
import dk.clausr.koncert.api.models.GroupDto
import dk.clausr.koncert.api.models.ProfileDto
import dk.clausr.koncert.data.UserPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userSettingDataSource: UserPreferencesDataSource,
    private val groupsApi: GroupsApi,
    private val profileApi: ProfileApi,
) {
    fun getUserData(): Flow<UserData?> = userSettingDataSource.userData.map {
        if (it.username.isBlank() && it.group.isBlank()) {
            null
        } else {
            it
        }
    }

    suspend fun setUserData(userData: UserData) {
        val username = createUsername(userData.username)
        val groupName = checkForGroupName(userData.group)
        userSettingDataSource.setUserPreferences(userData)
    }

    suspend fun setKeyboardHeight(height: Float) {
        userSettingDataSource.setKeyboardHeight(height)
    }

    suspend fun checkForGroupName(name: String): GroupDto {
        return groupsApi.joinOrCreate(name)
    }

    suspend fun createUsername(profileName: String): ProfileDto {
        return profileApi.getOrCreateProfile(profileName)
    }

}