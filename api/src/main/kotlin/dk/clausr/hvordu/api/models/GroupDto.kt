package dk.clausr.hvordu.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("friendly_name")
    val friendlyName: String,
)
