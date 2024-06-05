package search_model

import models.TypeDto

data class SearchTypeDto (
    val numberOfType: Int,
    val type: List<TypeDto>
)