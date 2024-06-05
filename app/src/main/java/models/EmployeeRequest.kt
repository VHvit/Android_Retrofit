package models

data class EmployeeRequest(
    val username: String,
    val password: String,
    val branchId: String
)
