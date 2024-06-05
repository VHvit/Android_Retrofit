package retrofit

import models.StatusDto
import models.TypeDto
import models.ViewDto
import retrofit2.Call
import retrofit2.http.*
import search_model.SearchStatusDto
import search_model.SearchTypeDto
import java.util.*

interface TypeRetrofit {

    @GET("type")
    fun getTypeList(): Call<List<TypeDto>>

    @DELETE("type/{typeId}")
    fun deleteTypeFromList(@Path("typeId") typeId: UUID): Call<Void>

    @GET("type/count")
    fun typeCount(): Call<Long>

    @GET("type/count/{typeId}")
    fun deleteTypeCount(@Path("typeId") typeId: UUID): Call<Int>

    @POST("type")
    fun createType(@Body typeDto: TypeDto): Call<TypeDto>

    @PUT("type/{typeId}")
    fun updateType(@Path("typeId") typeId: UUID, @Body typeDto: TypeDto): Call<TypeDto>

    @GET("type/search")
    fun searchType(@Query("query") query: String): Call<SearchTypeDto>
}