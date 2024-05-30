package dk.clausr.hvordu.repo.userdata

import dk.clausr.core.dispatchers.Dispatcher
import dk.clausr.core.dispatchers.Dispatchers
import dk.clausr.core.models.UserData
import dk.clausr.hvordu.api.GroupsApi
import dk.clausr.hvordu.api.ProfileApi
import dk.clausr.hvordu.api.models.GroupDto
import dk.clausr.hvordu.api.models.ProfileDto
import dk.clausr.hvordu.data.UserPreferencesDataSource
import dk.clausr.hvordu.repo.domain.Group
import dk.clausr.hvordu.repo.domain.toGroup
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SignOutScope
import io.github.jan.supabase.gotrue.providers.Google
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userSettingDataSource: UserPreferencesDataSource,
    private val groupsApi: GroupsApi,
    private val profileApi: ProfileApi,
    private val auth: Auth,
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    fun getUserData(): Flow<UserData> = userSettingDataSource.userData

    val sessionStatus = auth.sessionStatus

    suspend fun setInitialUsername(username: String) = withContext(ioDispatcher) {
        try {
            createUsername(username)
            true
        } catch (e: Exception) {
            Timber.e(e, "Coulnd't create username $username")
            false
        }
    }

    suspend fun joinOrCreateChatRoom(chatRoomName: String): Group = withContext(ioDispatcher) {
        val chatRoom = groupsApi.joinOrCreate(chatRoomName).toGroup()
        userSettingDataSource.addChatRoomId(chatRoom.id)
        chatRoom
    }

    suspend fun setKeyboardHeight(height: Float) {
        userSettingDataSource.setKeyboardHeight(height)
    }

    private suspend fun createUsername(profileName: String): ProfileDto {
        return profileApi.getOrCreateProfile(profileName)
    }

    suspend fun setFcmToken(token: String) {
        withContext(ioDispatcher) {
            profileApi.updateFcmToken(token)
        }
    }

    suspend fun getUsername(userId: String): String? = withContext(ioDispatcher) {
        val res = profileApi.getProfile(userId)
        Timber.d("Profile: $res")
        res?.username
    }

    suspend fun getGroups(): List<Group> = withContext(ioDispatcher) {
        groupsApi.getAllChatRoomsGlobally().map(GroupDto::toGroup)
    }

    suspend fun setLastVisitedChatRoom(chatRoomId: String) = withContext(ioDispatcher) {
        userSettingDataSource.setLastVisitedChatRoom(chatRoomId)
    }

    suspend fun signInWithGoogle(): Boolean {
        return try {
            auth.signInWith(Google)
            true
        } catch (e: Exception) {
            Timber.e(e, "Could not sign in with google...")
            false
        }
    }

    suspend fun signOut(): Boolean {
        return try {
            auth.signOut(SignOutScope.GLOBAL)
            true
        } catch (e: Exception) {
            Timber.e(e, "Couldn't sign out")
            false
        }
    }
}