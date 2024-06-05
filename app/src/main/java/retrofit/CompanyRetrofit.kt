package retrofit

import models.CompanyDto
import retrofit2.Call
import retrofit2.http.*
import search_model.SearchCompanyDto
import java.util.*

interface CompanyRetrofit {
    @GET("company")
    fun getCompanyList(): Call<List<CompanyDto>>

    @DELETE("company/{companyId}")
    fun deleteCompanyFromList(@Path("companyId") companyId: UUID): Call<Void>

    @GET("company/count")
    fun companyCount(): Call<Long>

    @GET("company/count/{companyId}")
    fun deleteCompanyCount(@Path("companyId") companyId: UUID): Call<Int>

    @POST("company")
    fun createCompany(@Body companyDto: CompanyDto): Call<CompanyDto>

    @PUT("company/{companyId}")
    fun updateCompany(@Path("companyId") companyId: UUID, @Body companyDto: CompanyDto): Call<CompanyDto>

    @GET("company/search")
    fun SearchCompany(
        @Query("companyName") companyName: String?,
        @Query("cityId") cityId: UUID?,
    ): Call<SearchCompanyDto>
}