package com.example.movieapp.popularMovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.R
import com.example.movieapp.single_movie_details.SingleMovie
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.data.api.TheMovieDbClient
import com.example.movieapp.data.api.TheMovieDbInterface

class MainActivity : AppCompatActivity() {
    lateinit var movieRepository: MoviePagedListRepo
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val apiService : TheMovieDbInterface = TheMovieDbClient.getClient()
        movieRepository = MoviePagedListRepo(apiService)
        viewModel = getViewModel()
        val movieAdapter = PopularMoviePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this,3)
        gridLayoutManager.spanSizeLookup = object :GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType : Int = movieAdapter.getItemViewType(position)
                if(viewType == movieAdapter.MOVIE_VIEW_TYPE) return 1
                else return 3
            }
        }
       var recyclerView :RecyclerView = findViewById(R.id.rv_movie_list)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = movieAdapter
        viewModel.moviePagedList.observe(this , Observer {
            movieAdapter.submitList(it)
        })
//        viewModel.networkState.observe(this, Observer {
//
//        })
    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
                    return MainActivityViewModel(movieRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.simpleName}")
            }
        }).get(MainActivityViewModel::class.java)
    }
}
