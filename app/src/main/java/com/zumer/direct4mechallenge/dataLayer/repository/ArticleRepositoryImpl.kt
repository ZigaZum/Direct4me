package com.zumer.direct4mechallenge.dataLayer.repository

import com.zumer.direct4mechallenge.dataLayer.ArticleApi
import com.zumer.direct4mechallenge.dataLayer.model.NewsResponse
import com.zumer.direct4mechallenge.util.Resource
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val api: ArticleApi
) : ArticleRepository {
    override suspend fun getLatestArticles(
        country: String,
    ): Resource<NewsResponse> {
        return try {
            val response = api.getLatestArticles(country)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
           Resource.Error(e.message ?: "An error occurred")
        }
    }

}