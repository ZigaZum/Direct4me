package com.zumer.direct4mechallenge.uiLayer

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.zumer.direct4mechallenge.TestDispatchers
import com.zumer.direct4mechallenge.dataLayer.repository.ArticleRepository
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MainVewModelTest {

    private lateinit var viewModel: MainVewModel
    private lateinit var articleRepository: ArticleRepository
    private lateinit var testDispatchers: TestDispatchers

    @Before
    fun setUp() {
        articleRepository = mockk<ArticleRepository>()
        testDispatchers = TestDispatchers()
        viewModel = MainVewModel(
            articleRepository,
            testDispatchers,
        )
    }

    @Test
    fun testApiEmptyResult() = runBlocking {
        val job = launch {
            viewModel.articles.test {
                val emission = awaitItem()
                assertThat(emission).isEqualTo(MainVewModel.ArticleEvent.Empty)
                cancelAndConsumeRemainingEvents()
            }
        }
        viewModel.setArticles(MainVewModel.ArticleEvent.Empty)
        job.join()
        job.cancel()
    }

    @Test
    fun testCountryNull() = runBlocking {
        val job = launch {
            viewModel.articles.test {
                val emission = awaitItem()
                assertThat(emission).isInstanceOf(MainVewModel.ArticleEvent.Failure("").javaClass)
                assertThat((emission as MainVewModel.ArticleEvent.Failure).errorText).isEqualTo("Invalid country abbreviation")
                cancelAndConsumeRemainingEvents()
            }
        }
        viewModel.getArticles(null)
        viewModel.getArticles("")
        viewModel.getArticles(" ")
        job.join()
        job.cancel()
    }
}