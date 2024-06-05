package search_model

import models.CompanyDto

data class SearchCompanyDto (
    val numberOfCompany: Int,
    val company: List<CompanyDto>
)