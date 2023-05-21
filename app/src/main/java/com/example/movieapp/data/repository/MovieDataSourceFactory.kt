package com.example.movieapp.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.movieapp.data.api.TheMovieDbInterface
import com.example.movieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(private val apiService:TheMovieDbInterface,private val compositeDisposable: CompositeDisposable) :DataSource.Factory<Int,Movie>(){
    val movieLiveDataSource = MutableLiveData<movieListDataSource>()
    override fun create(): DataSource<Int, Movie> {
        val movieListDatasource = movieListDataSource(apiService,compositeDisposable)
        movieLiveDataSource.postValue(movieListDatasource)
        return movieListDatasource
    }
}