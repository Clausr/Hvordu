package dk.clausr.hvordu.data

import androidx.datastore.core.DataStore
import dk.clausr.core.models.UserData
import dk.clausr.hvordu.data.modelhelpers.toModel
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesDataSource @Inject constructor(
    private val userPreferenceDataStore: DataStore<UserPreferences>,
) {
    val userData = userPreferenceDataStore.data.map {
        UserData(
            username = it.userName,
            profileId = it.profileId.ifBlank { null },
            chatRoomIds = it.chatRoomIdsList,
            lastVisitedChatRoomId = null, // TODO Set this?
            keyboardHeightState = it.keyboardHeightState.toModel(it.keyboardHeight)
        )
    }

    suspend fun setUsername(username: String, profileId: String) {
        userPreferenceDataStore.updateData { currentData ->
            currentData.toBuilder()
                .setUserName(username)
                .setProfileId(profileId)
                .build()
        }
    }

    suspend fun setInitialChatRoom(chatRoomId: String) {
        userPreferenceDataStore.updateData { currentData ->
            currentData.toBuilder()
                .addChatRoomIds(chatRoomId)
                .build()
        }
    }

    suspend fun setKeyboardHeight(keyboardHeight: Float) {
        userPreferenceDataStore.updateData { currentData ->
            currentData.toBuilder()
                .setKeyboardHeightState(UserPreferences.KeyboardHeightStateProto.KNOWN)
                .setKeyboardHeight(keyboardHeight)
                .build()
        }
    }

    suspend fun addChatRoomId(chatRoomId: String) {
        userPreferenceDataStore.updateData {
            if (!it.chatRoomIdsList.contains(chatRoomId)) {
                it.toBuilder()
                    .addChatRoomIds(chatRoomId)
                    .build()
            } else {
                Timber.w("Chat rooms already contained $chatRoomId")
                it
            }
        }
    }

    suspend fun deleteChatRoomId(id: String) {
        userPreferenceDataStore.updateData {
            if (it.chatRoomIdsList.contains(id)) {
                val filteredList = it.chatRoomIdsList.apply {
                    remove(id)
                }
                it.toBuilder()
                    .clearChatRoomIds()
                    .addAllChatRoomIds(filteredList)
                    .build()
            } else {
                Timber.w("Chat rooms didn't contain $id")
                it
            }
        }
    }


    suspend fun setLastVisitedChatRoom(chatRoomId: String?) {
        userPreferenceDataStore.updateData {
            it.toBuilder()
                .apply {
                    if (chatRoomId == null) {
                        clearLastVisitedChatRoomId()
                    } else {
                        setLastVisitedChatRoomId(chatRoomId)
                    }
                }
                .build()
        }
    }

    suspend fun setProfileId(profileId: String) {
        userPreferenceDataStore.updateData {
            it.toBuilder()
                .setProfileId(profileId)
                .build()
        }
    }
}