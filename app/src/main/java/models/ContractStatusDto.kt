package models

import java.util.UUID

data class ContractStatusDto (
    var contractStatusId: UUID,
    var value: String
)