package search_model

import models.ViewDto

data class SearchViewDto (
    val numberOfView: Int,
    val view: List<ViewDto>
)