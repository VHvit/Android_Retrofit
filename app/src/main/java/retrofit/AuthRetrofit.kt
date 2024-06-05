package retrofit

import retrofit2.Call
import retrofit2.http.*
import java.util.*


interface AuthRetrofit {
    @GET("/auth")
    fun authCon(): Call<Void?>?
}