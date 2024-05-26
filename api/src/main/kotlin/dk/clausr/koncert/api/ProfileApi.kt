package dk.clausr.koncert.api

import dk.clausr.koncert.api.models.ProfileDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import timber.log.Timber
import javax.inject.Inject

class ProfileApi @Inject constructor(
    client: SupabaseClient
) {
    private val table = client.postgrest["profiles"]

    var cachedProfileId: String? = null

    suspend fun getOrCreateProfile(profileName: String): ProfileDto {
        val profile = table.select {
            filter {
                eq("username", profileName)
            }
        }.decodeSingleOrNull<ProfileDto>() ?: createProfile(profileName)

        cachedProfileId = profile.id

        return profile
    }

    suspend fun getProfileId(username: String): String? {
        return cachedProfileId ?: (table.select {
            filter {
                eq("username", username)
            }
        }.decodeSingleOrNull() as ProfileDto?).apply {
            cachedProfileId = this?.id
        }?.id
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
        return table.insert(buildJsonObject {
            put("username", username)
        }) {
            select()
        }.decodeSingle()
    }
}
