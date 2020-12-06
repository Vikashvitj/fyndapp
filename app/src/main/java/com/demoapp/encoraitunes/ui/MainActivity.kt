package com.demoapp.encoraitunes.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.demoapp.encoraitunes.EncoraApp
import com.demoapp.encoraitunes.data_classes.Entry
import com.demoapp.encoraitunes.data_classes.TopSongs
import com.demoapp.encoraitunes.databinding.ActivityMainBinding
import com.demoapp.encoraitunes.util.ApiErrorModel
import com.demoapp.encoraitunes.util.NetworkUtils
import com.demoapp.encoraitunes.util.Utility
import com.demoapp.encoraitunes.util.extension.longToast
import com.demoapp.encoraitunes.util.extension.snack
import com.demoapp.encoraitunes.util.viewmodel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private val mViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(
            this,
            Utility.getViewModelFactory(
                MainActivityViewModel::class.java,
                MainActivityViewModel(
                    EncoraApp.appContainer.iTunesApiService,
                    EncoraApp.appContainer.appDatabase
                )
            )
        ).get(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Songs"
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        mBinding.mainViewModel = mViewModel
        setContentView(mBinding.root)
        setImageAdapter()
        observeApiCall()
        fetchTopSongs()
    }

    private fun observeApiCall() {
        mViewModel.apiResponse.observe(this, Observer {
            when (it) {
                is TopSongs -> {
                    supportActionBar?.title = it.feed.title.label
                    mViewModel.apiResponse.value = null
                }
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
            mViewModel.getTopSongs()
        } else {
            mBinding.root.snack("No connection found", Snackbar.LENGTH_LONG) {
                this.setAction("Retry") {
                    fetchTopSongs()
                }
            }
        }
    }


    private fun setImageAdapter() {
        val adapter = ImageListAdapter { item ->
            redirectToDetail(item)
        }
        val layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        mBinding.recycler.layoutManager = layoutManager
        mBinding.recycler.adapter = adapter
        mViewModel.list.observe(this, Observer {
            mViewModel.isEmptyList.set(it.isNullOrEmpty())
            adapter.submitList(mViewModel.list.value)
        })
    }

    private fun redirectToDetail(
        item: Entry
    ) {
        val intent = Intent(this, SongDetailActivity::class.java)
        intent.putExtra(SongDetailActivity.SONG_ID, item.id.attributes.imId)
        intent.putExtra(SongDetailActivity.SONG_TITLE, item.title?.label)
        startActivity(intent)
    }
}

