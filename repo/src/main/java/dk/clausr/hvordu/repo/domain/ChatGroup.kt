package dk.clausr.hvordu.repo.domain

import dk.clausr.hvordu.api.models.GroupDto

data class Group(
    val id: String,
    val friendlyName: String,
)

fun GroupDto.toGroup(): Group {
    return Group(
        id = id,
        friendlyName = friendlyName
    )
}