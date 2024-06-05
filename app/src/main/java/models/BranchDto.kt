package models

import java.util.UUID

data class BranchDto (
    var branchId: UUID,
    var branchName: String,
    var city: CityDto,
    var address: String,
    var branchPhone: String,
    var employeesCount: String,
    var company: CompanyDto,
)