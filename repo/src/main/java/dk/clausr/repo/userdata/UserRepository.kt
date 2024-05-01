package dk.clausr.repo.userdata

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dk.clausr.core.models.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    @ApplicationContext val context: Context,
) {

    // Use datastore instead, this is rubbish
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    fun getUserData(): Flow<UserData?> = flow {
        val prefUserName = sharedPreferences.getString(USERNAME, null)
        val prefGroup = sharedPreferences.getString(GROUP, null)
        if (prefUserName != null && prefGroup != null) {
            emit(UserData(prefUserName, prefGroup))
        } else {
            emit(null)
        }
    }

    fun setUserData(userData: UserData) {
        sharedPreferences.edit {
            putString(USERNAME, userData.username)
            putString(GROUP, userData.group)
        }
    }

    private val USERNAME = "username"
    private val GROUP = "group"
}