package et.edu.aait.retrofitexample

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CourseWebService{

    companion object {

        private val baseUrl = "http://10.0.2.2:9090/api/"
        @Volatile private var instance: CourseApiService? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: buildApiService().also { instance = it }
            }

       private fun buildApiService():CourseApiService {

           val interceptor = HttpLoggingInterceptor()
           interceptor.level = HttpLoggingInterceptor.Level.BASIC
           val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

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