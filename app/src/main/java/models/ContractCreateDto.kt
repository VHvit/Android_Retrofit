package models

import java.util.UUID

data class ContractCreateDto (
    var contractId: UUID,
    var summ: String,
    var date: String,
    var clientId: UUID,
    var view: ViewDto,
    var contractStatus: ContractStatusDto
)