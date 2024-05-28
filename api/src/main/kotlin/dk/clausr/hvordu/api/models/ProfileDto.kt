package dk.clausr.hvordu.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val id: String,
    val username: String,
    @SerialName("fcm_token")
    val fcmToken: String? = null,
)

@Serializable
data class InsertProfileUsernameDto(
    val username: String,
)

