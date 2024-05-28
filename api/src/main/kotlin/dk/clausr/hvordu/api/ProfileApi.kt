package dk.clausr.hvordu.api

import dk.clausr.hvordu.api.models.InsertProfileUsernameDto
import dk.clausr.hvordu.api.models.ProfileDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import timber.log.Timber
import javax.inject.Inject

class ProfileApi @Inject constructor(
    private val client: SupabaseClient
) {
    private val table = client.postgrest["profiles"]

    suspend fun getOrCreateProfile(profileName: String): ProfileDto {
        val profile = table.select {
            filter {
                eq("username", profileName)
            }
        }.decodeSingleOrNull<ProfileDto>() ?: createProfile(profileName)

        return profile
    }

    suspend fun getProfile(loginId: String): ProfileDto? {
        Timber.d("Try to get profile with id: $loginId")
        return table.select {
            filter {
                eq("id", loginId)
            }
        }.decodeSingleOrNull<ProfileDto>()
    }

    private suspend fun createProfile(username: String): ProfileDto {
        return table.insert(InsertProfileUsernameDto(username)) { select() }
            .decodeSingle<ProfileDto>()
    }

    suspend fun updateFcmToken(token: String) {
        try {
            val myProfile = table.select() {
                filter {
                    (ProfileDto::id eq client.auth.currentUserOrNull()?.id)
                }
            }.decodeSingle<ProfileDto>()
            table.upsert(myProfile.copy(fcmToken = token))
        } catch (e: Exception) {
            Timber.e("Could not update firebase token")
        }
    }
}
