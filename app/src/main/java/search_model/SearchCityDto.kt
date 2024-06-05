package search_model

import models.CityDto

data class SearchCityDto (
    val numberOfCities: Int,
    val city: List<CityDto>
)