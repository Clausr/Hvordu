package dk.clausr.repo.domain

import dk.clausr.koncert.api.models.GroupDto

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