package dk.clausr.hvordu.api

import dk.clausr.hvordu.api.models.CreateGroupDto
import dk.clausr.hvordu.api.models.GroupDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject

class GroupsApi @Inject constructor(
    client: SupabaseClient
) {
    private val table = client.postgrest["groups"]

    suspend fun joinOrCreate(groupName: String): GroupDto = table.select {
        filter {
            GroupDto::friendlyName eq groupName
        }
    }.decodeSingleOrNull<GroupDto>() ?: createGroup(groupName)

    suspend fun getAllChatRoomsGlobally(): List<GroupDto> = table.select().decodeList()

    suspend fun getChatRoom(chatRoomId: String): GroupDto? = table.select {
        filter {
            GroupDto::id eq chatRoomId
        }
    }.decodeSingleOrNull()

    suspend fun getChatRooms(chatRoomIds: List<String>): List<GroupDto> = table.select {
        filter {
            GroupDto::id isIn chatRoomIds
        }
    }.decodeList()

    private suspend fun createGroup(friendlyName: String): GroupDto {
        return table.insert(CreateGroupDto(friendlyName)) {
            select()
        }.decodeSingle<GroupDto>()
    }
}