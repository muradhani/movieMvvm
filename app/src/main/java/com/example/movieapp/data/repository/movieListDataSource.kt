package com.example.movieapp.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.movieapp.data.api.FIRST_PAGE
import com.example.movieapp.data.api.TheMovieDbInterface
import com.example.movieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class movieListDataSource(private val apiService:TheMovieDbInterface,private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int,Movie>() {
    private var page = FIRST_PAGE
    val networkState:MutableLiveData<NetworkState> = MutableLiveData()
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.totalPages >= params.key){
                            callback.onResult(it.movies,params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }else{
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        it.message?.let { it1 -> Log.e("movie data source", it1) }
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { response ->
                        val movies = response.movies
                        Log.d("error","theeresponse :"+response.page)
                        if (movies != null) {
                            callback.onResult(movies, null, page + 1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.ERROR)
                            Log.e("movie data source", "Movies response is null")
                        }
                    },
                    { throwable ->
                        networkState.postValue(NetworkState.ERROR)
                        throwable.message?.let { Log.e("movie data source", it) }
                    }
                )

        )
    }
}