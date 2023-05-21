package com.example.movieapp.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.api.POSTER_BASE_URL
import com.example.movieapp.data.api.TheMovieDbClient
import com.example.movieapp.data.api.TheMovieDbInterface
import com.example.movieapp.data.repository.NetworkState
import com.example.movieapp.data.vo.MovieDetails
import java.text.NumberFormat
import java.util.*


class SingleMovie : AppCompatActivity() {
    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieDetailsRepo: MovieDetailsRepo

    lateinit var movie_title : TextView
    lateinit var movie_tagline : TextView
    lateinit var movie_release_date : TextView
    lateinit var movie_rating : TextView
    lateinit var movie_overview : TextView
    lateinit var runtime : TextView
    lateinit var budget : TextView
    lateinit var revenue : TextView
    lateinit var error : TextView
    lateinit var poster : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)
        movie_title = findViewById(R.id.movie_title)
        movie_tagline = findViewById(R.id.movie_tagline)
        movie_release_date = findViewById(R.id.release_date_val)
        movie_rating = findViewById(R.id.rating)
        movie_overview = findViewById(R.id.overview)
        runtime = findViewById(R.id.runtime)
        budget = findViewById(R.id.budget)
        revenue = findViewById(R.id.revenue)
        error = findViewById(R.id.txt_error)
        poster = findViewById(R.id.posterimg)
        var progressbar: ProgressBar = findViewById(R.id.progress_bar)
        val movieid : Int = intent.getIntExtra("id",1)
        val apiservice : TheMovieDbInterface = TheMovieDbClient.getClient()
        movieDetailsRepo = MovieDetailsRepo(apiservice)
        viewModel = getViewModel(movieid)
        viewModel.movieDetails.observe(this, Observer {
            bindUi(it)
        })
        viewModel.networkState.observe(this, Observer {
            progressbar.visibility = if (it == NetworkState.LOADING)View.VISIBLE else View.GONE
            error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }
    fun bindUi(it: MovieDetails){
        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_release_date.text = it.releaseDate
        //movie_rating.text = it.rating.toString()
        movie_overview.text = it.overview
        runtime.text = it.runtime.toString()
        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
       // budget.text = formatCurrency.format(it.budget).toString()
        revenue.text = formatCurrency.format(it.revenue).toString()

        val moviePosterUrl = POSTER_BASE_URL + it.posterPath
        Glide.with(this).load(moviePosterUrl).into(poster)
        Log.d("test ",it.title)
    }
    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SingleMovieViewModel::class.java)) {
                    return SingleMovieViewModel(movieDetailsRepo, movieId) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
            }
        }
        return ViewModelProvider(this, factory).get(SingleMovieViewModel::class.java)
    }

}