package com.example.newsappproject.api

import com.example.newsappproject.models.NewsResponse
import com.example.newsappproject.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getLatestNews(
        @Query("country") countryCode: String = "us",
        @Query("page") pageNumber: Int =1,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") searchQuery: String ,
        @Query("page") pageNumber: Int =1,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>
}