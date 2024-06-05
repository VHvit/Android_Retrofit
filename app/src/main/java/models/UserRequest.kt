package models

import java.util.UUID

data class UserRequest(
    val username: String,
    val password: String
)
