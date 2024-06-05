package search_model

import models.ContractStatusDto

data class SearchContractStatusDto (
    val numberOfContractStatus: Int,
    val contractStatus: List<ContractStatusDto>
)