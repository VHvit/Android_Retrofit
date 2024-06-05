package retrofit

import models.CityDto
import models.StatusDto
import models.TypeDto
import retrofit2.Call
import retrofit2.http.*
import search_model.SearchContractStatusDto
import search_model.SearchStatusDto
import java.util.*

interface StatusRetrofit {

    @GET("status")
    fun getStatusList(): Call<List<StatusDto>>

    @DELETE("status/{statusId}")
    fun deleteStatusFromList(@Path("statusId") statusId: UUID): Call<Void>

    @GET("status/count")
    fun statusCount(): Call<Long>

    @GET("status/count/{statusId}")
    fun deleteStatusCount(@Path("statusId") statusId: UUID): Call<Int>

    @POST("status")
    fun createStatus(@Body statusDto: StatusDto): Call<StatusDto>

    @PUT("status/{statusId}")
    fun updateStatus(@Path("statusId") statusId: UUID, @Body statusDto: StatusDto): Call<StatusDto>

    @GET("status/search")
    fun searchStatus(@Query("query") query: String): Call<SearchStatusDto>
}