package dk.clausr.hvordu.api

import dk.clausr.hvordu.api.models.ChatRoomDto
import dk.clausr.hvordu.api.models.GroupDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import javax.inject.Inject

class OverviewApi @Inject constructor(
    private val supabaseClient: SupabaseClient
) {
    suspend fun getOverviewItems(chatRoomIds: List<String>): List<ChatRoomDto> {
        val table = supabaseClient.postgrest["messages"]

        val groups = supabaseClient.postgrest["groups"]
            .select {
                filter {
                    isIn("id", chatRoomIds)
                }
            }.decodeList<GroupDto>()

        val latestMessages = groups.mapNotNull {
            table.select(
                Columns.raw(
                    """
                latest_message:content, 
                id:group_id, 
                latest_message_at:created_at, 
                image_url, 
                ...profiles(latest_sender:username), 
                ...groups(room_name:friendly_name)
                """.trimIndent()
                )
            ) {
                filter {
                    eq("group_id", it.id)
                }
                order("created_at", Order.DESCENDING)
                limit(1)
            }.decodeSingleOrNull<ChatRoomDto>()
        }

        return latestMessages
    }
}