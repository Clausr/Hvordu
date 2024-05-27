package dk.clausr.hvordu.api

import dk.clausr.hvordu.api.models.GroupDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class GroupsApi @Inject constructor(
    client: SupabaseClient
) {
    private val table = client.postgrest["groups"]

    suspend fun joinOrCreate(groupName: String): GroupDto {
        return table.select {
            filter {
                eq("friendly_name", groupName)
            }
        }.decodeSingleOrNull<GroupDto>() ?: createGroup(groupName)
    }

    suspend fun getAllChatRoomsGlobally(): List<GroupDto> = table.select().decodeList()

    suspend fun getGroup(groupName: String): GroupDto? = table.select {
        filter {
            eq("friendly_name", groupName)
        }
    }.decodeSingleOrNull()

    suspend fun getChatRoom(chatRoomId: String): GroupDto? = table.select {
        filter {
            eq("id", chatRoomId)
        }
    }.decodeSingleOrNull()

    suspend fun getChatRooms(chatRoomIds: List<String>): List<GroupDto> = table.select {
        filter {
            isIn("id", chatRoomIds)
        }
    }.decodeList()

    private suspend fun createGroup(friendlyName: String): GroupDto {
        return table.insert(buildJsonObject {
            put("friendly_name", friendlyName)
        }) {
            select()
        }.decodeSingle()
    }
}