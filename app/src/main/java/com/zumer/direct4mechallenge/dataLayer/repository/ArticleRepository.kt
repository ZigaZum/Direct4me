package com.zumer.direct4mechallenge.dataLayer.repository

import com.zumer.direct4mechallenge.dataLayer.model.NewsResponse
import com.zumer.direct4mechallenge.util.Resource

interface ArticleRepository {
    suspend fun getLatestArticles(country: String): Resource<NewsResponse>
}