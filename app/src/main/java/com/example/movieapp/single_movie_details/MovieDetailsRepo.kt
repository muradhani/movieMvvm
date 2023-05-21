package com.example.movieapp.single_movie_details

import androidx.lifecycle.LiveData
import com.example.movieapp.data.api.TheMovieDbInterface
import com.example.movieapp.data.repository.MovieDatasource
import com.example.movieapp.data.repository.NetworkState
import com.example.movieapp.data.vo.MovieDetails
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MovieDetailsRepo (private val apiservice : TheMovieDbInterface){
    lateinit var movieDatasource: MovieDatasource
    fun fetchingSingleMovieDetails(compositeDisposable: io.reactivex.disposables.CompositeDisposable,movieId:Int):LiveData<MovieDetails>{
        movieDatasource = MovieDatasource(apiservice,compositeDisposable)
        movieDatasource.fetchMovieDetails(movieId)
        return movieDatasource.downloadedMovieResponse
    }
    fun getMovieNetworkState():LiveData<NetworkState>{
        return movieDatasource.networkState
    }
}