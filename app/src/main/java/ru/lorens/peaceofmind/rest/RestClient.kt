package ru.lorens.peaceofmind.rest

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

object RestClient {

    val getClient: ApiInterface by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://oooo.su/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofit.create(ApiInterface::class.java)
    }

    interface ApiInterface {
        @GET("dasha.php")
        suspend fun getDates(): Respons

        @POST("dasha.php")
        suspend fun setDate(@Body date: Date): Respons

        @POST("dasha.php")
        suspend fun setDate(@Body date: List<Date>): Respons
    }
}