package dk.clausr.repo.userdata

import dk.clausr.core.dispatchers.Dispatcher
import dk.clausr.core.dispatchers.Dispatchers
import dk.clausr.core.models.UserData
import dk.clausr.koncert.api.GroupsApi
import dk.clausr.koncert.api.ProfileApi
import dk.clausr.koncert.api.models.GroupDto
import dk.clausr.koncert.api.models.ProfileDto
import dk.clausr.koncert.data.UserPreferencesDataSource
import dk.clausr.repo.domain.Group
import dk.clausr.repo.domain.toGroup
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userSettingDataSource: UserPreferencesDataSource,
    private val groupsApi: GroupsApi,
    private val profileApi: ProfileApi,
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    fun getUserData(): Flow<UserData> = userSettingDataSource.userData

    suspend fun setInitialUsername(username: String) {
        val profile = createUsername(username)
        userSettingDataSource.setUsername(
            username = profile.username,
            profileId = profile.id
        )
    }

    suspend fun setInitialChatRoom(chatRoomName: String) {
        val chatRoom = joinOrCreateChatRoom(name = chatRoomName)
        userSettingDataSource.setInitialChatRoom(chatRoom.id)
    }

    suspend fun setKeyboardHeight(height: Float) {
        userSettingDataSource.setKeyboardHeight(height)
    }

    suspend fun joinOrCreateChatRoom(name: String): Group {
        return groupsApi.joinOrCreate(name).toGroup()
    }

    private suspend fun createUsername(profileName: String): ProfileDto {
        return profileApi.getOrCreateProfile(profileName)
    }

    suspend fun getGroups(): List<Group> = withContext(ioDispatcher) {
        groupsApi.getAllChatRoomsGlobally().map(GroupDto::toGroup)
    }

    suspend fun setLastVisitedChatRoom(chatRoomId: String) = withContext(ioDispatcher) {
        userSettingDataSource.setLastVisitedChatRoom(chatRoomId)
    }

    suspend fun setProfileId(profileId: String) = withContext(ioDispatcher) {
        userSettingDataSource.setProfileId(profileId)
    }


}