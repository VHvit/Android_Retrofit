package retrofit;

import models.ClientDto
import retrofit2.Call
import retrofit2.http.*
import search_model.SearchClientDto
import java.util.*

public interface ClientRetrofit {

    @GET("client")
    fun getClientList(): Call<List<ClientDto>>

    @DELETE("client/{clientId}")
    fun deleteClientFromList(@Path("clientId") clientId: UUID): Call<Void>

    @GET("client/count")
    fun clientCount(): Call<Long>

    @GET("client/count/{clientId}")
    fun deleteClientCount(@Path("clientId") clientId: UUID): Call<Int>

    @POST("client")
    fun createClient(@Body clientDto: ClientDto): Call<ClientDto>

    @PUT("client/{clientId}")
    fun updateClient(@Path("clientId") clientId: UUID, @Body clientDto: ClientDto): Call<ClientDto>

    @GET("/client/{statusId}")
    fun getClientByStatusId(@Path("statusId") statusId: UUID): Call<SearchClientDto>

    @GET("client/search")
    fun SearchClient(
        @Query("firstname") firstname: String?,
        @Query("lastname") lastname: String?,
        @Query("middlename") middlename: String?,
        @Query("birthday") birthday: String?
    ): Call<SearchClientDto>
}
