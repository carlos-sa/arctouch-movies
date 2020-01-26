package com.arctouch.codechallenge.model

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.presenter.MoviesDetailsContract
import com.arctouch.codechallenge.presenter.MoviesListContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object MoviesRepository {

    private var totalPages = 1

    private val api: TmdbApi = Retrofit.Builder()
            .baseUrl(TmdbApi.URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TmdbApi::class.java)

    init {

        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                    Cache.updateMoviesGenres()
                }

    }

    fun loadMoviesPage(presenter: MoviesListContract.OnMovieResponseCallback, pageNumber: Long) {
        if (totalPages < pageNumber) {
            presenter.onError("All the pages are already loaded")
        } else {
            api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, pageNumber, TmdbApi.DEFAULT_REGION)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        val moviesWithGenres = it.results.map { movie ->
                            movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                        }
                        totalPages = it.totalPages
                        Cache.cacheMovies(moviesWithGenres)
                        presenter.onResponse(moviesWithGenres)
                    }
        }
    }

    fun loadMovieDetails(presenter: MoviesDetailsContract.OnMovieResponseCallback, id: Int) {
        val movie = Cache.movies.find {it.id == id}
        if (movie !== null) presenter.onResponse(movie) else presenter.onError("There was an error fetching the movies details");
    }

}

