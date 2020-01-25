package com.arctouch.codechallenge.presenter

import com.arctouch.codechallenge.model.Movie

interface MoviesListContract {

    interface View {
        fun showProgress()

        fun hideProgress()

        fun showMoviesList(moviesList: List<Movie>)

        fun updateMoviesList(moviesList: List<Movie>)

        fun showErrorMessage(message:String)
    }

    interface Presenter {

        fun loadMoviesPage()
    }

    interface OnMovieResponseCallback {

        fun onResponse(moviesList: List<Movie>)
        fun onError(errorMessage: String)
    }
}