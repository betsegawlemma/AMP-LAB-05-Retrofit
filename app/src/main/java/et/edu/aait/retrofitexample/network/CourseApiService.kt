package et.edu.aait.retrofitexample.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface CourseApiService {

    @GET("courses/{id}")
    fun findByIdAsync(@Path("id") id: Long): Deferred<Response<Course>>
    @GET("courses")
    fun findByCodeAsync(@Query("code") code: String): Deferred<Response<Course>>
    @POST("courses")
    fun insertCourseAsync(@Body newCourse: Course): Deferred<Response<Void>>
    @PUT("courses/{id}")
    fun updateCourseAsnc(@Path("id") id: Long, @Body newCourse: Course): Deferred<Response<Void>>
    @DELETE("courses/{id}")
    fun deleteCourseAsync(@Path("id") id: Long): Deferred<Response<Void>>


    companion object {

        private val baseUrl = "http://10.0.2.2:9090/api/"

        fun getInstance(): CourseApiService {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .build()

            val retrofit: Retrofit =  Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()

            return retrofit.create(CourseApiService::class.java)
        }
    }
}





















