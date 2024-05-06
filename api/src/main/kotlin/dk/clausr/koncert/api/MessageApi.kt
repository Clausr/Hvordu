package dk.clausr.koncert.api

import dk.clausr.koncert.api.models.MessageDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class MessageApi @Inject constructor(
    client: SupabaseClient
) {
    private val table = client.postgrest["rartis"]

    suspend fun retrieveMessages(): List<MessageDto> = table.select().decodeList()

    suspend fun createMessage(content: String): MessageDto {
        return table.insert(buildJsonObject {
            put("content", content)
            put("creator_id", "Clausr")
        }) {
            select()
        }.decodeSingle()
    }

    suspend fun deleteMessage(id: Int) {
        table.delete {
            filter {
                MessageDto::id eq id
            }
        }
    }
}