package com.arctouch.codechallenge.presenter

import com.arctouch.codechallenge.model.Movie

interface MoviesDetailsContract {

    interface View {
        fun showProgress()

        fun hideProgress()

        fun showMovieDetails(movie: Movie?)

        fun showErrorMessage(message:String)

        fun finishPage()
    }

    interface Presenter {

        fun loadMovieDetails(id:Int)
    }

    interface OnMovieResponseCallback {

        fun onResponse(movie: Movie?)
        fun onError(errorMessage: String)
    }
}