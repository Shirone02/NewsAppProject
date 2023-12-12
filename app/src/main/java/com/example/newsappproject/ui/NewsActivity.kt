package com.example.newsappproject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newsappproject.R
import com.example.newsappproject.database.ArticleDatabase
import com.example.newsappproject.databinding.ActivityNewsBinding
import com.example.newsappproject.repository.NewsRepository
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsActivity : AppCompatActivity() {

    lateinit var newsViewModel: NewsViewModel
    lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelFactory = NewsViewModelFactory(application, newsRepository)
        newsViewModel = ViewModelProvider(this, viewModelFactory).get(NewsViewModel::class.java)

        initView()

    }
    private fun initView(){
//        val bnvMenu = findViewById<BottomNavigationView>(R.id.bnvMenu)
//        val navController = findNavController(R.id.fragment)
//
//        bnvMenu.setupWithNavController(navController)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnvMenu.setupWithNavController(navController)
        
    }
}