package retrofit

import models.CityDto
import models.TypeDto
import models.ViewDto
import retrofit2.Call
import retrofit2.http.*
import search_model.SearchTypeDto
import search_model.SearchViewDto
import java.util.*

interface ViewRetrofit {

    @GET("view")
    fun getViewList(): Call<List<ViewDto>>

    @DELETE("view/{viewId}")
    fun deleteViewFromList(@Path("viewId") viewId: UUID): Call<Int>

    @GET("view/count")
    fun viewCount(): Call<Long>

    @GET("view/count/{viewId}")
    fun deleteViewCount(@Path("viewId") viewId: UUID): Call<Int>

    @POST("view")
    fun createView(@Body viewDto: ViewDto): Call<ViewDto>

    @PUT("view/{viewId}")
    fun updateView(@Path("viewId") viewId: UUID, @Body viewDto: ViewDto): Call<ViewDto>

    @GET("view/search")
    fun searchView(@Query("query") query: String): Call<SearchViewDto>
}