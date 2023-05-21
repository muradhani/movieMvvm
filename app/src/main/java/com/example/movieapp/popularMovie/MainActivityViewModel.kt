package com.example.movieapp.popularMovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.movieapp.data.repository.NetworkState
import com.example.movieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(private val movieRepo: MoviePagedListRepo):ViewModel() {
    private val compositeDisposible = CompositeDisposable()
    val moviePagedList : LiveData<PagedList<Movie>> by lazy {
        movieRepo.fetchLiveMoviePagedList(compositeDisposible)
    }
    val networkState : LiveData<NetworkState> by lazy {
        movieRepo.getNetworkState()
    }
    fun ListIsEmpty():Boolean{
        return moviePagedList.value?.isEmpty()?:true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposible.dispose()
    }
}