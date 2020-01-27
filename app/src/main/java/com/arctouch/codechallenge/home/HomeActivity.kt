package com.arctouch.codechallenge.home

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.details.DetailsActivity
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.presenter.MoviesListContract
import com.arctouch.codechallenge.presenter.MoviesListPresenter
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity(), MoviesListContract.View, OnLoadMore {

    private lateinit var moviesListPresenter: MoviesListPresenter
    private val SCROLL_POSTION_TAG: String  = "POSTION_TAG"
    lateinit var infiniteScrollListener: OnInfiniteScrollListener
    lateinit var moviesList: MutableList<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        createViewPresenter()

        if (savedInstanceState != null) {
            moviesListPresenter.onScreenRotate(savedInstanceState.getInt(SCROLL_POSTION_TAG))
        } else {
            moviesListPresenter.loadMoviesPage()
        }
    }

    override fun onDestroy() {
        moviesListPresenter.deatachView()
        super.onDestroy()
    }

    private fun createViewPresenter() {
        if (lastCustomNonConfigurationInstance !== null) {
            moviesListPresenter = lastCustomNonConfigurationInstance as MoviesListPresenter
        } else
            moviesListPresenter = MoviesListPresenter()
        moviesListPresenter.attachView(this)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return moviesListPresenter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.search_button -> {
                onSearchButton()
                true
            }
            R.id.clear_button -> {
                onClearButton()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun onClearButton() {
        moviesListPresenter.onClearButton()
    }

    // Deal with received intents
    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            doSearch(intent.getStringExtra(SearchManager.QUERY))
        }
    }

    private fun onSearchButton() {
        onSearchRequested() // Call search Widget
    }

    private fun doSearch(query: String) {
        moviesListPresenter.onMovieSearch(query)
    }
    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putInt(SCROLL_POSTION_TAG, recyclerView.scrollState)
        super.onSaveInstanceState(outState, outPersistentState)
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

    override fun scrollMovieList(position: Int) {
        recyclerView.scrollToPosition(position)
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
