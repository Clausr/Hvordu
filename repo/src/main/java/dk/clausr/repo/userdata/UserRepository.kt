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
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
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

    suspend fun getGroups(): List<Group> = withContext(ioDispatcher) {
        groupsApi.getAllChatRoomsGlobally().map(GroupDto::toGroup)
    }

    suspend fun setLastVisitedChatRoom(chatRoomId: String) = withContext(ioDispatcher) {
        userSettingDataSource.setLastVisitedChatRoom(chatRoomId)
    }

    suspend fun setProfileId(profileId: String) = withContext(ioDispatcher) {
        userSettingDataSource.setProfileId(profileId)
    }

    suspend fun signIn(email: String, password: String): Boolean {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            true
        } catch (e: Exception) {
            Timber.e(e, "Could not sign in")
            false
        }
    }

    suspend fun signUp(email: String, password: String): Boolean {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            true
        } catch (e: Exception) {
            Timber.e(e, "Could not sign up")
            false
        }
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

}