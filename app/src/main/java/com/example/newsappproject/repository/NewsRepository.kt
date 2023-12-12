package com.example.newsappproject.repository

import com.example.newsappproject.api.RetrofitInstance
import com.example.newsappproject.database.ArticleDatabase
import com.example.newsappproject.models.Article
import retrofit2.http.Query

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getLatestNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getLatestNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getFavoriteNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}