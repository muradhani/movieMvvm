package com.example.movieapp.data.repository
enum class Status{
    RUNNING,
    SUCCESS,
    FAILED
}
class NetworkState(val status: Status , val msg :String){
    companion object{
        val LOADED : NetworkState
        val LOADING :NetworkState
        val ERROR : NetworkState
        val ENDOFLIST :NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS,"success")
            LOADING = NetworkState(Status.RUNNING,"Running")
            ERROR = NetworkState(Status.FAILED,"something went wrong")
            ENDOFLIST = NetworkState(Status.FAILED,"you have reach the end")
        }
    }
}
