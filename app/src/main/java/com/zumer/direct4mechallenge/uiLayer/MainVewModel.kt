package com.zumer.direct4mechallenge.uiLayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zumer.direct4mechallenge.dataLayer.model.Article
import com.zumer.direct4mechallenge.dataLayer.repository.ArticleRepository
import com.zumer.direct4mechallenge.util.DispatcherProvider
import com.zumer.direct4mechallenge.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVewModel @Inject constructor(
    private val repository: ArticleRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class ArticleEvent {
        class Success(val resultText: String, val data: List<Article>): ArticleEvent()
        class Failure(val errorText: String): ArticleEvent()
        object Loading : ArticleEvent()
        object Empty : ArticleEvent()
    }

    sealed class SelectedArticle {
        class Selected(val article: Article): SelectedArticle()
        object Empty : SelectedArticle()
    }

    private val _selectedArticle = MutableStateFlow<SelectedArticle>(SelectedArticle.Empty)
    val selectedArticle = _selectedArticle.asStateFlow()


    /**
     * @param SelectedArticle
     * Sets different selected articles for sharing between fragments and helps with navigation
     */
    fun setSelectedArticle(selectedArticle: SelectedArticle) {
        viewModelScope.launch(dispatchers.main) {
            _selectedArticle.emit(selectedArticle)
        }
    }

    sealed class SelectedTheme {
        object Light : SelectedTheme()
        object Dark : SelectedTheme()
    }

    private val _selectedTheme = MutableStateFlow<SelectedTheme>(SelectedTheme.Light)
    val selectedTheme = _selectedTheme.asStateFlow()

    /**
     * @param SelectedTheme
     * Sets dark or light theme. Used for Dark mode implementation
     */
    fun setSelectedTheme(selectedTheme: SelectedTheme) {
        viewModelScope.launch(dispatchers.main) {
            _selectedTheme.emit(selectedTheme)
        }
    }

    private val _articles = MutableStateFlow<ArticleEvent>(ArticleEvent.Empty)
    val articles: StateFlow<ArticleEvent> = _articles.asStateFlow()

    fun getArticles(
        country: String?,
    ) {
        if (country.isNullOrBlank()) {
            _articles.value = ArticleEvent.Failure("Invalid country abbreviation")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _articles.value = ArticleEvent.Loading
            when (val newsResponse = repository.getLatestArticles(country)) {
                is Resource.Error -> _articles.value = ArticleEvent.Failure(newsResponse.message!!)
                is Resource.Success -> {
                    val articles = newsResponse.data?.articles
                    if (articles == null) {
                        _articles.value = ArticleEvent.Failure("Unexpected error")
                    } else {
                        _articles.value = ArticleEvent.Success("", articles)
                    }
                }
            }
        }
    }

    /**
     * @param ArticleEvent
     * Used for testing
     */
    fun setArticles(articleEvent: ArticleEvent) {
        viewModelScope.launch(dispatchers.main) {
            _articles.emit(articleEvent)
        }
    }
}