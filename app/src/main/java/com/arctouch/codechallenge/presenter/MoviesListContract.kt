package com.arctouch.codechallenge.presenter

import com.arctouch.codechallenge.model.Movie

interface MoviesListContract {

    interface view {
        fun showProgress()

        fun hideProgress()

        fun showMoviesList(moviesList: List<Movie>)
    }

    interface presenter {

        fun loadMoviesList()
    }

    interface OnMovieResponseCallback {

        fun onResponse(moviesList: List<Movie>)
        fun onError(errorMessage: String)
    }
}