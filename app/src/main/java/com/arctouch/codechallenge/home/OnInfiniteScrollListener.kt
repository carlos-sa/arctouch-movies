package com.arctouch.codechallenge.home

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.arctouch.codechallenge.model.Movie

interface OnLoadMore{
    fun loadMoreMovies()
}

interface OnLoadPageFinished {
    fun onPageLoaded(moviesList: List<Movie>)
}

class OnInfiniteScrollListener(val layoutManager: LinearLayoutManager,
                               val adapter: RecyclerView.Adapter<HomeAdapter.ViewHolder>,
                               var moviesList: MutableList<Movie>,
                               var paginationActivity: OnLoadMore):
        RecyclerView.OnScrollListener(), OnLoadPageFinished {

    var previousTotal = 0
    var loading = true
    val visibleThreshold = 10
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    var initialSize = 0

    lateinit var recyclerView: RecyclerView

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        this.recyclerView = recyclerView

        visibleItemCount = recyclerView.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            initialSize = moviesList.size
            updateDataList(moviesList)
        }
    }

    fun updateDataList(moviesList: MutableList<Movie>) {
        paginationActivity.loadMoreMovies()
    }

    override fun onPageLoaded(moviesList: List<Movie>) {
        this.moviesList.addAll(moviesList)
        val updatedSize = moviesList.size
        recyclerView.post { adapter.notifyItemRangeInserted(initialSize, updatedSize) }
        loading = true
    }
}