package models

import java.util.UUID

data class ContractUpdateDto (
    var contractId: UUID,
    var contractStatus: ContractStatusDto
)