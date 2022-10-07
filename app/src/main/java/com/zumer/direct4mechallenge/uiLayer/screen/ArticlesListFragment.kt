package com.zumer.direct4mechallenge.uiLayer.screen

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.zumer.direct4mechallenge.R
import com.zumer.direct4mechallenge.databinding.FragmentArticlesListBinding
import com.zumer.direct4mechallenge.uiLayer.MainVewModel
import com.zumer.direct4mechallenge.uiLayer.adapter.ArticlesListAdapter
import com.zumer.direct4mechallenge.uiLayer.enum.Country


class ArticlesListFragment : Fragment(R.layout.fragment_articles_list){
    private lateinit var binding: FragmentArticlesListBinding

    private val viewModel: MainVewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade_out)
        enterTransition = inflater.inflateTransition(R.transition.fade_in)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticlesListBinding.bind(view)

        initViews()
    }

    private fun initViews() {
        val rwAdapter = ArticlesListAdapter(ArticlesListAdapter.OnClickListener { article ->
            viewModel.setSelectedArticle(MainVewModel.SelectedArticle.Selected(article))
        })
        binding.recyclerview.apply {
            setHasFixedSize(true)
            adapter = rwAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, Country.values()).
            also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spCountry.adapter = adapter
            } }

        binding.btArticles.setOnClickListener {
            viewModel.getArticles((binding.spCountry.selectedItem as Country).countryCode)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.articles.collect { event ->
                when(event) {
                    is MainVewModel.ArticleEvent.Success -> {
                        binding.progressBar.isVisible = false
                        rwAdapter.submitList(event.data)
                    }
                    is MainVewModel.ArticleEvent.Failure -> {
                        binding.progressBar.isVisible = false
                    }
                    is MainVewModel.ArticleEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }

        val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        binding.swTheme.isChecked = isNightTheme == Configuration.UI_MODE_NIGHT_YES

        binding.swTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setSelectedTheme(MainVewModel.SelectedTheme.Dark)
            } else {
                viewModel.setSelectedTheme(MainVewModel.SelectedTheme.Light)
            }
        }
    }
}