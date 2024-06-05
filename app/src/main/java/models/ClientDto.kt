package models

import java.util.UUID

data class ClientDto (
    var clientId: UUID,
    var firstname: String,
    var lastname: String,
    var middlename: String,
    var birthday: String,
    var phone: String,
    var status: StatusDto
)