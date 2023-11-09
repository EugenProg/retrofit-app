package kz.just_code.retorfitapp.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApiData {
    private const val baseUrl = "https://api.weatherapi.com/v1/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getInterceptors())
            .build()
    }

    fun getApi(): WeatherApi {
        return getRetrofit()
            .create(WeatherApi::class.java)
    }

    private fun getInterceptors(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(KeyInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()
    }
}

class KeyInterceptor: Interceptor {
    private val weatherApiKey = "bf4dc83bbe114579817143928230611"
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url
            .newBuilder()
            .addQueryParameter("key", weatherApiKey)
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}