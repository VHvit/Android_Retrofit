package retrofit

import models.ContractStatusDto
import retrofit2.Call
import retrofit2.http.*
import search_model.SearchContractStatusDto
import java.util.*

interface ContractStatusRetrofit {
    @GET("contractStatus")
    fun getContractStatusList(): Call<List<ContractStatusDto>>

    @DELETE("contractStatus/{contractStatusId}")
    fun deleteContractStatusFromList(@Path("contractStatusId") contractStatusId: UUID): Call<Void>

    @GET("contractStatus/count")
    fun contractStatusCount(): Call<Long>

    @GET("contractStatus/count/{contractStatusId}")
    fun deleteContractStatusCount(@Path("contractStatusId") contractStatusId: UUID): Call<Int>

    @POST("contractStatus")
    fun createContractStatus(@Body contractStatusDto: ContractStatusDto): Call<ContractStatusDto>

    @PUT("contractStatus/{contractStatusId}")
    fun updateContractStatus(@Path("contractStatusId") contractStatusId: UUID, @Body contractStatusDto: ContractStatusDto): Call<ContractStatusDto>

    @GET("contractStatus/search")
    fun searchContractStatus(@Query("query") query: String): Call<SearchContractStatusDto>
}