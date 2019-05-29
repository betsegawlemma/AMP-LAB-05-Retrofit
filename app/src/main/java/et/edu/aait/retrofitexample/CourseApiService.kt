package et.edu.aait.retrofitexample

import kotlinx.coroutines.Deferred
import retrofit2.Response
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

}





















