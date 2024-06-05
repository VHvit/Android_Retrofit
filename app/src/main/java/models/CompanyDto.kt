package models

import java.util.UUID

data class CompanyDto (
    var companyId: UUID,
    var companyName: String,
    var licenseYear: String,
    var city: CityDto,
    var phone: String,
    var type: TypeDto
)