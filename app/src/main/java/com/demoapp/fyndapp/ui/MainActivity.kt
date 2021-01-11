package com.demoapp.fyndapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demoapp.fyndapp.TMDBApp
import com.demoapp.fyndapp.data_classes.Movie
import com.demoapp.fyndapp.databinding.ActivityMainBinding
import com.demoapp.fyndapp.util.ApiErrorModel
import com.demoapp.fyndapp.util.EndlessRecyclerViewScrollListener
import com.demoapp.fyndapp.util.NetworkUtils
import com.demoapp.fyndapp.util.Utility
import com.demoapp.fyndapp.util.extension.longToast
import com.demoapp.fyndapp.util.extension.snack
import com.demoapp.fyndapp.util.viewmodel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private val mViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(
            this,
            Utility.getViewModelFactory(
                MainActivityViewModel::class.java,
                MainActivityViewModel(
                    TMDBApp.appContainer.iTunesApiService,
                    TMDBApp.appContainer.appDatabase
                )
            )
        ).get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Movies"
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        mBinding.mainViewModel = mViewModel
        setContentView(mBinding.root)
        setImageAdapter()
        setSearchListener()
        observeApiCall()
        fetchTopSongs()
    }

    private fun setSearchListener() {
        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mViewModel.page = 1
                searchMovie(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        mBinding.cancelSearch.setOnClickListener {
            clearSearchResult()
        }
    }

    private fun clearSearchResult() {
        mViewModel.searchQuery.set("")
        mBinding.searchView.setQuery("", true)
        mViewModel.page = 1
        mViewModel.getTopMovies()
    }

    private fun observeApiCall() {
        mViewModel.apiResponse.observe(this, Observer {
            when (it) {
                is ApiErrorModel -> {
                    this.longToast(it.message ?: it.errorCode ?: "")
                    mViewModel.apiResponse.value = null
                }
                is Throwable -> {
                    this.longToast(it.message ?: "something went wrong")
                    mViewModel.apiResponse.value = null
                }
            }
        })
    }

    private fun fetchTopSongs() {
        if (NetworkUtils.isConnectionAvailable(this)) {
            mViewModel.getTopMovies()
        } else {
            mBinding.root.snack("No connection found", Snackbar.LENGTH_LONG) {
                this.setAction("Retry") {
                    fetchTopSongs()
                }
            }
        }
    }

    private fun searchMovie(query: String) {
        if (NetworkUtils.isConnectionAvailable(this)) {
            mViewModel.searchMovie(query)
        } else {
            mBinding.root.snack("No connection found", Snackbar.LENGTH_LONG) {
                this.setAction("Retry") {
                    searchMovie(query)
                }
            }
        }
    }


    private fun setImageAdapter() {
        val adapter = ImageListAdapter { item ->
            redirectToDetail(item)
        }
        val layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        mBinding.recycler.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (mViewModel.isEmptyList.get()!!
                        .not() && mViewModel.totalRecords > totalItemsCount && mViewModel.totalPages > mViewModel.page
                ) {
                    mViewModel.page = mViewModel.page + 1
                    if (mViewModel.searchQuery.get().isNullOrEmpty())
                        fetchTopSongs()
                    else
                        searchMovie(mViewModel.searchQuery.get()!!)
                }
            }
        })
        mBinding.recycler.layoutManager = layoutManager
        mBinding.recycler.adapter = adapter
        mViewModel.list.observe(this, Observer {
            mViewModel.isEmptyList.set(it.isNullOrEmpty())
            adapter.submitList(mViewModel.list.value)
        })
    }

    private fun redirectToDetail(
        item: Movie
    ) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.SONG_ID, item.id)
        intent.putExtra(MovieDetailActivity.SONG_TITLE, item.originalTitle)
        startActivity(intent)
    }
}

