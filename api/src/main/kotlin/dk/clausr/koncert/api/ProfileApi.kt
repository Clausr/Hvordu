package dk.clausr.koncert.api

import dk.clausr.koncert.api.models.ProfileDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class ProfileApi @Inject constructor(
    client: SupabaseClient
) {
    private val table = client.postgrest["profiles"]

    suspend fun getOrCreateProfile(profileName: String): ProfileDto = table.select {
        filter {
            eq("username", profileName)
        }
    }.decodeSingleOrNull<ProfileDto>() ?: createProfile(profileName)

    suspend fun getProfile(username: String): ProfileDto? = table.select {
        filter {
            eq("username", username)
        }
    }.decodeSingleOrNull()

    private suspend fun createProfile(username: String): ProfileDto {
        return table.insert(buildJsonObject {
            put("username", username)
        }) {
            select()
        }.decodeSingle()
    }
}
