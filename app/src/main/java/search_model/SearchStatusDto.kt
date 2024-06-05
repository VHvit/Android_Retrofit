package search_model

import models.StatusDto

data class SearchStatusDto (
    val numberOfStatus: Int,
    val status: List<StatusDto>
)