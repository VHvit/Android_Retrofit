package retrofit

import models.BranchDto
import retrofit2.Call
import retrofit2.http.*
import search_model.SearchBranchDto
import java.util.*

interface BranchRetrofit {
    @GET("branch")
    fun getBranchList(): Call<List<BranchDto>>

    @DELETE("branch/{branchId}")
    fun deleteBranchFromList(@Path("branchId") branchId: UUID): Call<Void>

    @GET("branch/count")
    fun branchCount(): Call<Long>

    @GET("branch/count/{branchId}")
    fun deleteBranchCount(@Path("branchId") branchId: UUID): Call<Int>

    @POST("branch")
    fun createBranch(@Body branchDto: BranchDto): Call<BranchDto>

    @PUT("branch/{branchId}")
    fun updateBranch(@Path("branchId") branchId: UUID, @Body branchDto: BranchDto): Call<BranchDto>

    @GET("branch/search")
    fun SearchBranch(
        @Query("branchName") branchName: String?,
        @Query("cityId") cityId: UUID?,
        @Query("companyId") companyId: UUID?,
    ): Call<SearchBranchDto>
}