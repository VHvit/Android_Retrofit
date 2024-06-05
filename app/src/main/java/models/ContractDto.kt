package models

import java.util.UUID

data class ContractDto (
    var contractId: UUID,
    var summ: String,
    var date: String,
    var client: ClientDto,
    var view: ViewDto,
    var user: UserDto,
    var branch: BranchDto,
    var contractStatus: ContractStatusDto
)