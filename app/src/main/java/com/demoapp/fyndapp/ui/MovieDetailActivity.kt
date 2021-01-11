package com.demoapp.fyndapp.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.demoapp.fyndapp.TMDBApp
import com.demoapp.fyndapp.databinding.ActivityImageDetailBinding
import com.demoapp.fyndapp.util.Utility
import com.demoapp.fyndapp.util.viewmodel.MovieDetailViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityImageDetailBinding
    private lateinit var mediaPlayer: SimpleExoPlayer
    private lateinit var dataSourceFactory: DefaultDataSourceFactory

    private val mViewModel: MovieDetailViewModel by lazy {
        ViewModelProvider(
            this,
            Utility.getViewModelFactory(
                MovieDetailViewModel::class.java,
                MovieDetailViewModel(
                    intent?.getIntExtra(SONG_ID, 0)!!,
                    TMDBApp.appContainer.appDatabase
                ),
            )
        ).get(MovieDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent?.getStringExtra(SONG_TITLE)!!
        mBinding = ActivityImageDetailBinding.inflate(layoutInflater)
        mBinding.songDetailViewModel = mViewModel
        setContentView(mBinding.root)
        bindViewClicks()
    }

    private fun bindViewClicks() {

    }

    private fun toggleSongPlay() {
        mediaPlayer.playWhenReady = mediaPlayer.playWhenReady.not()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val SONG_ID = "song_ID"
        const val SONG_TITLE = "song_title"
    }
}