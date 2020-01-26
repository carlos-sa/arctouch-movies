package com.arctouch.codechallenge.presenter

import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.MoviesRepository

class MoviesListPresenter(moviesView: MoviesListContract.View): MoviesListContract.Presenter, MoviesListContract.OnMovieResponseCallback {

    private val moviesRepository: MoviesRepository = MoviesRepository
    private var page: Long = 1  // Stores the current Page number
    private var loading = false // To avoid reloading the same page
    private var moviesView = moviesView

    override fun loadMoviesPage() {
        if (!loading) {
            moviesRepository.loadMoviesPage(this, page)
            loading = true
        }
    }

    override fun onResponse(moviesList: List<Movie>) {
        if (1L == page++) {
            moviesView.showMoviesList(moviesList)
            moviesView.hideProgress()
        } else {
            moviesView.updateMoviesList(moviesList)
        }
        loading = false
    }

    override fun onError(errorMessage: String) {
        moviesView.showErrorMessage(errorMessage)
    }
}