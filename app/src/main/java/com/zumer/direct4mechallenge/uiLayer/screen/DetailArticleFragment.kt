package com.zumer.direct4mechallenge.uiLayer.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.transition.TransitionInflater
import coil.compose.rememberAsyncImagePainter
import com.zumer.direct4mechallenge.R
import com.zumer.direct4mechallenge.dataLayer.model.Article
import com.zumer.direct4mechallenge.uiLayer.MainVewModel
import com.zumer.direct4mechallenge.util.DateFormatter

class DetailArticleFragment : Fragment() {
    private val viewModel: MainVewModel by activityViewModels()

    /**
     * Implemented for back navigation and transition
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.setSelectedArticle(MainVewModel.SelectedArticle.Empty)
        }
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                CustomTheme {
                    ArticleDetail()
                }
            }
        }
    }

    @Composable
    private fun isNightMode() = when (AppCompatDelegate.getDefaultNightMode()) {
        AppCompatDelegate.MODE_NIGHT_NO -> false
        AppCompatDelegate.MODE_NIGHT_YES -> true
        else -> isSystemInDarkTheme()
    }

    @Composable
    private fun CustomTheme(
        isDark: Boolean = isNightMode(),
        content: @Composable () -> Unit
    ){
        MaterialTheme(
            colors = if (isDark) darkColors() else lightColors(),
            content = content
        )
    }

    @Composable
    private fun MyTopAppBar() {
        TopAppBar(
            title = { Text(
                text = "Details",
                fontFamily = FontFamily(Font(R.font.jost_variablefont_wght)),
                fontSize = 24.sp )},
            navigationIcon = {
                IconButton(onClick = { viewModel.setSelectedArticle(MainVewModel.SelectedArticle.Empty) }) {
                    Icon(Icons.Filled.ArrowBack, "backIcon")
                }
            },
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White,
            elevation = 10.dp,
        )
    }

    @Composable
    private fun MyContent(selectedArticle: Article, padding: PaddingValues) {
        Column(modifier = Modifier
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = selectedArticle.urlToImage),
                    contentDescription = selectedArticle.description,
                    modifier = Modifier.size(200.dp, 200.dp)
                )
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = selectedArticle.author ?: "",
                        fontFamily = FontFamily(Font(R.font.jost_italic_variablefont_wght)),
                        fontSize = 16.sp
                    )
                    Text(
                        text = DateFormatter.formatDateFromString(selectedArticle.publishedAt, "dd. MM. yyyy HH:mm"),
                        fontFamily = FontFamily(Font(R.font.jost_variablefont_wght)),
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = selectedArticle.description,
                fontFamily = FontFamily(Font(R.font.jost_variablefont_wght)),
                fontSize = 16.sp
            )
        }
    }

    @Composable
    private fun ArticleDetail() {
        val article by viewModel.selectedArticle.collectAsState()
        var selectedArticle: MainVewModel.SelectedArticle.Selected? = null

        if (article is MainVewModel.SelectedArticle.Selected) {
            selectedArticle = article as MainVewModel.SelectedArticle.Selected
        }

        selectedArticle?.let {
            Scaffold(
                topBar = { MyTopAppBar() },
            )
            { paddingValues  -> MyContent(selectedArticle = selectedArticle.article, paddingValues ) }
        }
    }
}