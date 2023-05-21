package com.example.movieapp.popularMovie

import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movieapp.data.api.POST_PER_PAGE
import com.example.movieapp.data.api.TheMovieDbInterface
import com.example.movieapp.data.repository.MovieDataSourceFactory
import com.example.movieapp.data.repository.MovieDatasource
import com.example.movieapp.data.repository.NetworkState
import com.example.movieapp.data.vo.Movie
import com.google.android.material.transformation.FabTransformationSheetBehavior
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepo(private val api:TheMovieDbInterface) {
    lateinit var moviePagedList:LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory
    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>> {
        movieDataSourceFactory = MovieDataSourceFactory(api, compositeDisposable)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()
        moviePagedList = LivePagedListBuilder(movieDataSourceFactory, config).build()
        return moviePagedList
    }

    fun getNetworkState():LiveData<NetworkState>{
        return movieDataSourceFactory.movieLiveDataSource.switchMap { movieDataSource ->
            movieDataSource.networkState
        }

    }
}