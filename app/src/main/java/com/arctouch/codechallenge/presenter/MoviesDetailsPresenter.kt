package com.arctouch.codechallenge.presenter

import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.MoviesRepository

class MoviesDetailsPresenter(detailsView: MoviesDetailsContract.View): MoviesDetailsContract.Presenter, MoviesDetailsContract.OnMovieResponseCallback {

    private val moviesRepository: MoviesRepository = MoviesRepository
    private var detailsView = detailsView


    override fun loadMovieDetails(id: Int) {
        moviesRepository.loadMovieDetails(this, id)
    }

    override fun onResponse(movie: Movie?) {
        detailsView.showMovieDetails(movie)
        detailsView.hideProgress()
    }

    override fun onError(errorMessage: String) {
        detailsView.showErrorMessage(errorMessage)
        detailsView.finishPage()
    }
}