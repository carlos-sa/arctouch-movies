package com.arctouch.codechallenge.details

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.presenter.MoviesDetailsContract
import com.arctouch.codechallenge.presenter.MoviesDetailsPresenter
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.details_activity.*

class DetailsActivity: AppCompatActivity(), MoviesDetailsContract.View {

    companion object {
        const val MOVIE_ID = "MOVIE_ID_TAG" // Intent Tag
    }

    private lateinit var movieDetailsPresenter: MoviesDetailsPresenter
    private val movieImageUrlBuilder = MovieImageUrlBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)

        movieDetailsPresenter = MoviesDetailsPresenter(this)

        // Retrive movie id
        val movieId = intent.getIntExtra(MOVIE_ID, -1)

        movieDetailsPresenter.loadMovieDetails(movieId)
    }


    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun showMovieDetails(movie: Movie?) {

        supportActionBar?.let{
            it.setDisplayHomeAsUpEnabled(true)
            it.title = movie?.title
        }

        genresTextView.text = movie?.genres?.joinToString(separator = ", ") { it.name }
        releaseDateTextView.text = movie?.releaseDate
        overviewTextView.text = movie?.overview

        Glide.with(this)
                .load(movie?.backdropPath?.let { movieImageUrlBuilder.buildBackdropUrl(it) })
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(backdropImageView)

        Glide.with(this)
                .load(movie?.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(posterImageView)
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun finishPage() {
        this.finish()
    }

    // Finish acitivty on back button
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onNavigateUp()
    }

}