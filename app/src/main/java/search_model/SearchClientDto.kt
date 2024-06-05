package search_model

import models.ClientDto

data class SearchClientDto (
    val numberOfClient: Int,
    val client: List<ClientDto>
)