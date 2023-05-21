package com.example.movieapp.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.data.repository.NetworkState
import com.example.movieapp.data.vo.MovieDetails
import io.reactivex.rxjava3.disposables.CompositeDisposable

class SingleMovieViewModel(private val movieDetailsRepo: MovieDetailsRepo,movieId:Int) :ViewModel() {
    private val compositeDisposable = io.reactivex.disposables.CompositeDisposable()
    val movieDetails : LiveData<MovieDetails> by lazy {
        movieDetailsRepo.fetchingSingleMovieDetails(compositeDisposable,movieId)
    }
    val networkState : LiveData<NetworkState> by lazy {
        movieDetailsRepo.getMovieNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}