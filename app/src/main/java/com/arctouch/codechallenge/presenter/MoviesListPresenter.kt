package com.arctouch.codechallenge.presenter

import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.MoviesRepository

class MoviesListPresenter(moviesView: MoviesListContract.view): MoviesListContract.presenter, MoviesListContract.OnMovieResponseCallback {

    private val moviesRepository: MoviesRepository = MoviesRepository()

    private var moviesView = moviesView

    override fun loadMoviesList() {
        moviesRepository.loadMoviesList(this)
    }

    override fun onResponse(moviesList: List<Movie>) {
        moviesView.showMoviesList(moviesList)
        moviesView.hideProgress()
    }

    override fun onError(errorMessage: String) {
        print("on error")
    }
}