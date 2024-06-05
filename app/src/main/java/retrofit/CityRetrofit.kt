package retrofit

import models.CityDto
import retrofit2.Call
import retrofit2.http.*
import search_model.SearchCityDto
import java.util.*

interface CityRetrofit {
    @GET("city")
    fun getCityList(): Call<List<CityDto>>

    @DELETE("city/{cityId}")
    fun deleteCityFromList(@Path("cityId") cityId: UUID): Call<Void>

    @GET("city/count")
    fun cityCount(): Call<Long>

    @GET("city/count/{cityId}")
    fun deleteCityCount(@Path("cityId") cityId: UUID): Call<Int>

    @POST("city")
    fun createCity(@Body cityDto: CityDto): Call<CityDto>

    @PUT("city/{cityId}")
    fun updateCity(@Path("cityId") cityId: UUID, @Body cityDto: CityDto): Call<CityDto>

    @GET("city/search")
    fun searchCities(@Query("query") query: String): Call<SearchCityDto>
}