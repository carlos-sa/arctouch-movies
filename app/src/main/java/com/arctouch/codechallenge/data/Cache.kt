package com.arctouch.codechallenge.data

import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie

object Cache {

    var genres = listOf<Genre>()

    var movies = mutableListOf<Movie>()

    fun cacheGenres(genres: List<Genre>) {
        this.genres = genres
    }

    fun cacheMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
    }

    fun updateMoviesGenres() {
        movies = movies.map { movie -> movie.copy(genres = genres.filter { movie.genreIds?.contains(it.id) == true }) }.toMutableList()
    }
}