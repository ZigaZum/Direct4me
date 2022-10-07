package com.zumer.direct4mechallenge.dataLayer

import com.zumer.direct4mechallenge.dataLayer.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApi {
    @GET("top-headlines")
    suspend fun getLatestArticles(
        @Query("country") country: String,
    ) : Response<NewsResponse>
}