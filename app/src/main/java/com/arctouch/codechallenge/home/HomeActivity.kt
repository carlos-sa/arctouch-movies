package com.arctouch.codechallenge.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.details.DetailsActivity
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.presenter.MoviesListContract
import com.arctouch.codechallenge.presenter.MoviesListPresenter
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : BaseActivity(), MoviesListContract.View, OnLoadMore {

    private lateinit var moviesListPresenter: MoviesListPresenter
    lateinit var infiniteScrollListener: OnInfiniteScrollListener
    lateinit var moviesList: MutableList<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        moviesListPresenter = MoviesListPresenter(this)
        moviesListPresenter.loadMoviesPage()

    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun showErrorMessage(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMoviesList(moviesList: List<Movie>) {

            this.moviesList = moviesList.toMutableList()
            val adapter = HomeAdapter(this.moviesList)
            adapter.onItemClick = { moviePosition:Int ->
                gotoDetailsActivity(moviePosition)
            }

            recyclerView.adapter = adapter
            infiniteScrollListener = OnInfiniteScrollListener(
                    recyclerView.layoutManager as LinearLayoutManager, adapter, this.moviesList, this)
            recyclerView.addOnScrollListener(infiniteScrollListener)

    }

    override fun updateMoviesList(moviesList: List<Movie>) {
        infiniteScrollListener.onPageLoaded(moviesList)
    }

    override fun loadMoreMovies() {
        moviesListPresenter.loadMoviesPage()
    }

    private fun gotoDetailsActivity(id: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.MOVIE_ID, id)
        startActivity(intent)
    }
}
