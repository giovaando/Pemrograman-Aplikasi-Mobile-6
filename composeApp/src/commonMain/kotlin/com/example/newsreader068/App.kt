package com.example.newsreader068

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsreader068.data.remote.HttpClientFactory
import com.example.newsreader068.data.remote.NewsApi
import com.example.newsreader068.data.repository.NewsRepository
import com.example.newsreader068.ui.screens.NewsDetailScreen
import com.example.newsreader068.ui.screens.NewsListScreen
import com.example.newsreader068.viewmodel.NewsDetailViewModel
import com.example.newsreader068.viewmodel.NewsListViewModel
import kotlin.reflect.KClass

object Routes {
    const val NEWS_LIST = "news_list"
    const val NEWS_DETAIL = "news_detail/{articleId}"
    fun newsDetail(id: Int) = "news_detail/$id"
}

@Composable
fun App() {
    val repository = remember {
        val httpClient = HttpClientFactory.create()
        val newsApi = NewsApi(httpClient)
        NewsRepository(newsApi)
    }

    MaterialTheme {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Routes.NEWS_LIST) {

            composable(route = Routes.NEWS_LIST) {
                val viewModel: NewsListViewModel = viewModel(
                    factory = newsListViewModelFactory(repository)
                )
                NewsListScreen(
                    viewModel = viewModel,
                    onArticleClick = { navController.navigate(Routes.newsDetail(it)) }
                )
            }

            composable(
                route = Routes.NEWS_DETAIL,
                arguments = listOf(navArgument("articleId") { type = NavType.IntType })
            ) { backStackEntry ->
                val articleId = backStackEntry.arguments?.getInt("articleId") ?: return@composable
                val viewModel: NewsDetailViewModel = viewModel(
                    factory = newsDetailViewModelFactory(repository)
                )
                NewsDetailScreen(
                    articleId = articleId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

private fun newsListViewModelFactory(repository: NewsRepository): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            @Suppress("UNCHECKED_CAST")
            return NewsListViewModel(repository) as T
        }
    }
}

private fun newsDetailViewModelFactory(repository: NewsRepository): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            @Suppress("UNCHECKED_CAST")
            return NewsDetailViewModel(repository) as T
        }
    }
}