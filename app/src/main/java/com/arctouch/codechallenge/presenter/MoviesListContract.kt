package com.arctouch.codechallenge.presenter

import com.arctouch.codechallenge.model.Movie

interface MoviesListContract {

    interface View {
        fun showProgress()

        fun hideProgress()

        fun showMoviesList(moviesList: List<Movie>)

        fun scrollMovieList(position: Int)

        fun updateMoviesList(moviesList: List<Movie>)

        fun showErrorMessage(message:String)
    }

    interface Presenter {

        fun loadMoviesPage()

        fun onScreenRotate(position: Int)

        fun onMovieSearch(query: String)

        fun onClearButton()

        fun attachView(view: View)

        fun deatachView()
    }

    interface OnMovieResponseCallback {

        fun onMovieListResponse(moviesList: List<Movie>)
        fun onMovieListError(errorMessage: String)
    }

    interface OnSearchResponseCallback {
        fun onSearchResponse(moviesList: List<Movie>)
        fun onSearchError(errorMessage: String)
    }
}