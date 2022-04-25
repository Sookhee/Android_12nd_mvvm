package com.mashup.mvvm

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mashup.mvvm.network.GithubApi
import com.mashup.mvvm.network.GithubInterceptor
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object ServiceLocator {
    private const val GITHUB_HOST_URL = "https://api.github.com"
    private const val TIME_OUT_DURATION_SECOND = 10L

    private fun getGithubRetrofitClient(): Retrofit {
        val contentType = MediaType.parse("application/json")
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(GithubInterceptor)
            .writeTimeout(TIME_OUT_DURATION_SECOND, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT_DURATION_SECOND, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(GITHUB_HOST_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType!!))
            .build()
    }

    fun getGithubApi(): GithubApi {
        return getGithubRetrofitClient().create(GithubApi::class.java)
    }
}