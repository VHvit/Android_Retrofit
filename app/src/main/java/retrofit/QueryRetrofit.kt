package retrofit

import models.EmployeeRequest
import models.UserRequest
import retrofit2.Call
import retrofit2.http.*
import java.util.*


interface QueryRetrofit {

    @GET("query/1_1/{status_id}")
    fun query1_1(@Path("status_id") statusId: UUID): Call<Int>

    @GET("query/1_2/{company_id}")
    fun query1_2(@Path("company_id") companyId: UUID): Call<Int>

    @GET("query/1_3/{date}")
    fun query1_3(@Path("date") date: String): Call<Int>

    @GET("query/1_4/{date}")
    fun query1_4(@Path("date") date: String): Call<Int>

    @GET("query/2_1")
    fun query2_1(): Call<Int>

    @GET("query/2_2")
    fun query2_2(): Call<Int>

    @GET("query/2_3")
    fun query2_3(): Call<Int>

    @GET("query/3")
    fun query3(): Call<Int>

    @GET("query/4")
    fun query4(): Call<Int>

    @GET("query/5/{branch_id}")
    fun query5(@Path("branch_id") branchId: UUID): Call<Int>

    @GET("query/6")
    fun query6(): Call<Int>

    @GET("query/7/{branch_id}")
    fun query7(@Path("branch_id") branchId: UUID): Call<List<Map<String, Any>>>

    @GET("query/8/{company_id}")
    fun query8(@Path("company_id") companyId: UUID): Call<List<Map<String, Any>>>

    @GET("query/9")
    fun query9(): Call<Int>

    @GET("query/11")
    fun query11(): Call<Int>

    @GET("query/12")
    fun query12(): Call<Map<String, Int>>

    @GET("query/13_1/{summ}")
    fun query13_1(@Path("summ") summ: String): Call<Int>

    @GET("query/13_2/{value}")
    fun query13_2(@Path("value") value: String): Call<Int>

    @GET("query/13_3/{date}")
    fun query13_3(@Path("date") date: String): Call<Int>

    @GET("query/13_4/{value}")
    fun query13_4(@Path("value") value: String): Call<Int>

    @GET("query/14_1/{firstname}")
    fun query14_1(@Path("firstname") firstname: String): Call<Int>

    @GET("query/14_2")
    fun query14_2(): Call<Int>

    @GET("query/14_3")
    fun query14_3(): Call<Int>

    @GET("query/14_4")
    fun query14_4(): Call<Int>

    @POST("query/createEmployee")
    fun createEmployee(@Body request: EmployeeRequest?): Call<Void>

    @POST("query/createAdmin")
    fun createAdmin(@Body request: UserRequest?): Call<Void>

    @POST("query/createStatist")
    fun createStatist(@Body request: UserRequest?): Call<Void>

    @PUT("query/updateUser")
    fun updateUser(@Body request: UserRequest?): Call<Void>

    @DELETE("query/deleteUser/{username}")
    fun deleteUser(@Path("username") username: String): Call<Void>

    @GET("query/employee")
    fun getEmployee(): Call<List<String>>

    @GET("query/admin")
    fun getAdmin(): Call<List<String>>

    @GET("query/statist")
    fun getStatist(): Call<List<String>>
}