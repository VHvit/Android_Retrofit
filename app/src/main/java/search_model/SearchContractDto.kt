package search_model

import models.ContractDto

data class SearchContractDto (
    val numberOfContract: Int,
    val contract: List<ContractDto>
)