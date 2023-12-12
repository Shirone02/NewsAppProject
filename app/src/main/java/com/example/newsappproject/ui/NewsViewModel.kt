package com.example.newsappproject.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsappproject.models.Article
import com.example.newsappproject.models.NewsResponse
import com.example.newsappproject.repository.NewsRepository
import com.example.newsappproject.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class NewsViewModel(app: Application, val newsRepository: NewsRepository): AndroidViewModel(app) {

    val latestNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var latestNewsPage = 1
    var latestNewsResponse: NewsResponse?= null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse?= null
    var newSearchQuery: String?= null
    var oldSearchQuery: String?= null

    init {
        getLatestNews("us")
    }

    fun getLatestNews(countryCode: String) = viewModelScope.launch {
        latestNewsInternet(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsInternet(searchQuery)
    }

    private fun handleLatestNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                latestNewsPage++
                if(latestNewsResponse == null) {
                    latestNewsResponse = resultResponse
                } else {
                    val oldArticles = latestNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(latestNewsResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                if(searchNewsResponse == null || newSearchQuery != oldSearchQuery){
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun addToFavorite(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getFavoriteNews() = newsRepository.getFavoriteNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun internetConnection(context: Context): Boolean{
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when{
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }

    private suspend fun latestNewsInternet(countryCode: String){
        latestNews.postValue(Resource.Loading())
        try{
            if(internetConnection(this.getApplication())){
                val response = newsRepository.getLatestNews(countryCode, latestNewsPage)
                latestNews.postValue(handleLatestNewsResponse(response))
            } else {
                latestNews.postValue(Resource.Error("No internet connection!"))
            }
        } catch(t: Throwable){
            when(t) {
                is IOException -> latestNews.postValue(Resource.Error("Unable to connect"))
                else -> latestNews.postValue(Resource.Error("No signal"))
            }
        }
    }

    private suspend fun searchNewsInternet(searchQuery: String){
        newSearchQuery = searchQuery
        searchNews.postValue(Resource.Loading())
        try {
            if(internetConnection(this.getApplication())){
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection!"))
            }
        } catch (t:Throwable){
            when(t){
                is IOException -> searchNews.postValue(Resource.Error("Unable to connect"))
                else -> searchNews.postValue(Resource.Error("No signal"))
            }
        }
    }
}

