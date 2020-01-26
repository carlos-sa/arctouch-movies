package com.arctouch.codechallenge.presenter

import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.MoviesRepository

class MoviesListPresenter(moviesView: MoviesListContract.View): MoviesListContract.Presenter,
        MoviesListContract.OnMovieResponseCallback,
        MoviesListContract.OnSearchResponseCallback {

    private val moviesRepository: MoviesRepository = MoviesRepository
    private var page: Long = 1  // Stores the current Page number
    private var searchPage: Long = 1
    private var loading = false // To avoid reloading the same page
    private var searchQuery: String? = null
    private var moviesView = moviesView

    override fun loadMoviesPage() {
        if (!loading) {
            if (searchQuery !== null) {
                moviesRepository.searchMoviesPage(this, searchQuery!!, searchPage)
            } else {
                moviesRepository.loadMoviesPage(this, page)
            }
            loading = true
        }
    }

    override fun onScreenRotate(position: Int) {

        if (searchQuery == null) moviesView.showMoviesList(moviesRepository.loadCachedSearchMovies())
        else moviesView.showMoviesList(moviesRepository.loadCachedMovies())

        moviesView.scrollMovieList(position)
        moviesView.hideProgress()
    }

    override fun onMovieSearch(query: String) {
        searchPage = 1
        searchQuery = query
        moviesRepository.searchMoviesPage(this, query, page)
        moviesView.showProgress()
    }

    override fun onClearButton() {
        searchQuery = null
        moviesRepository.clearSearchMovie()
        moviesView.showMoviesList(moviesRepository.loadCachedMovies())
    }

    override fun onMovieListResponse(moviesList: List<Movie>) {
        if (1L == page++) {
            moviesView.showMoviesList(moviesList)
            moviesView.hideProgress()
        } else {
            moviesView.updateMoviesList(moviesList)
        }
        loading = false
    }

    override fun onMovieListError(errorMessage: String) {
        moviesView.showErrorMessage(errorMessage)
    }

    override fun onSearchResponse(moviesList: List<Movie>) {
        if (1L == searchPage++) {
            moviesView.showMoviesList(moviesList)
            moviesView.hideProgress()
        } else {
            moviesView.updateMoviesList(moviesList)
        }
        loading = false
    }

    override fun onSearchError(errorMessage: String) {
        moviesView.hideProgress()
        moviesView.showErrorMessage(errorMessage)
    }
}