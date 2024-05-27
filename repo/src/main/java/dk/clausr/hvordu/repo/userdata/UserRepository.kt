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
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SignOutScope
import io.github.jan.supabase.gotrue.auth
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
    private val supabaseClient: SupabaseClient,
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    fun getUserData(): Flow<UserData> = userSettingDataSource.userData

    val sessionStatus = auth.sessionStatus

    suspend fun setInitialUsername(username: String) {
        val profile = createUsername(username)
        userSettingDataSource.setUsername(
            username = profile.username,
            profileId = profile.id
        )
    }

    suspend fun setInitialChatRoom(chatRoomName: String): Group = withContext(ioDispatcher) {
        val chatRoom = joinOrCreateChatRoom(name = chatRoomName)
        userSettingDataSource.setInitialChatRoom(chatRoom.id)
        chatRoom
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

    suspend fun setProfileId(profileId: String) = withContext(ioDispatcher) {
        userSettingDataSource.setProfileId(profileId)
    }

    suspend fun signInWithGoogle(): Boolean {
        return try {
            supabaseClient.auth.signInWith(Google)
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