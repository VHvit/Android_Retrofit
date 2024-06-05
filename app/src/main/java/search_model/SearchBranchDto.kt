package search_model

import models.BranchDto

data class SearchBranchDto (
    val numberOfBranch: Int,
    val branch: List<BranchDto>
)