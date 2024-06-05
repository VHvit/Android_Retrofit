package retrofit

import models.*
import retrofit2.Call
import retrofit2.http.*
import search_model.SearchContractDto
import java.util.*

interface ContractRetrofit {

    @GET("contract")
    fun getContractList(): Call<List<ContractDto>>

    @DELETE("contract/{contractId}")
    fun deleteContractFromList(@Path("contractId") contractId: UUID): Call<Void>

    @GET("/contract/count")
    fun contractCount(): Call<Long>

    @GET("contract/count/{contractId}")
    fun deleteContractCount(@Path("contractId") contractId: UUID): Call<Int>

    @POST("contract")
    fun createContract(@Body contractCreateDto: ContractCreateDto): Call<ContractCreateDto>

    @PUT("contract/{contractId}")
    fun updateContract(@Path("contractId") contractId: UUID, @Body contractUpdateDto: ContractUpdateDto): Call<ContractUpdateDto>

    @GET("contract/search")
    fun SearchContracts(
        @Query("summ") summ: String?,
        @Query("date") date: String?,
        @Query("viewId") viewId: UUID?,
        @Query("contractStatusId") contractStatusId: UUID?
    ): Call<SearchContractDto>

    @GET("/contract/{clientId}")
    fun getContractsByClientId(@Path("clientId") clientId: UUID): Call<SearchContractDto>

}