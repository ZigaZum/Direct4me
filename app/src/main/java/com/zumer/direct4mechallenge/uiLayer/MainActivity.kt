package com.zumer.direct4mechallenge.uiLayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuProvider
import androidx.lifecycle.lifecycleScope
import com.zumer.direct4mechallenge.R
import com.zumer.direct4mechallenge.databinding.ActivityMainBinding
import com.zumer.direct4mechallenge.uiLayer.screen.ArticlesListFragment
import com.zumer.direct4mechallenge.uiLayer.screen.DetailArticleFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainVewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCurrentTheme()

        setCurrentFragment()
    }

    /**
     * Used for setting night mode depending on switch selection
     */
    private fun setCurrentTheme() {
        lifecycleScope.launchWhenStarted {
            viewModel.selectedTheme.collect { event ->
                if (event is MainVewModel.SelectedTheme.Light) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }
    }

    /**
     * Observing status of SelectedArticle and implements fragment transition
     */
    private fun setCurrentFragment() {
        lifecycleScope.launchWhenStarted {
            viewModel.selectedArticle.collect { event ->
                if (event is MainVewModel.SelectedArticle.Selected) {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, DetailArticleFragment())
                        commit()
                    }
                } else {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, ArticlesListFragment())
                        commit()
                    }
                }
            }
        }
    }
}