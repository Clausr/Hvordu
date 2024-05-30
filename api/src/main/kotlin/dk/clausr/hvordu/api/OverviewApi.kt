package dk.clausr.hvordu.api

import dk.clausr.hvordu.api.models.ChatRoomDto
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

        return table.select(columns = Columns.raw("id:group_id, latest_message:content, latest_message_at:created_at, ...profiles(latest_sender:username), ...groups(room_name:friendly_name)")) {
            filter {
                this.isIn("group_id", chatRoomIds)
            }
            order("created_at", order = Order.DESCENDING)
            limit(1)
        }.decodeList<ChatRoomDto>()
    }
}