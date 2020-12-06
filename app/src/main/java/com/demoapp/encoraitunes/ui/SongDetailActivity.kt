package com.demoapp.encoraitunes.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.demoapp.encoraitunes.EncoraApp
import com.demoapp.encoraitunes.R
import com.demoapp.encoraitunes.databinding.ActivityImageDetailBinding
import com.demoapp.encoraitunes.util.Utility
import com.demoapp.encoraitunes.util.viewmodel.SongDetailViewModel
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class SongDetailActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityImageDetailBinding
    private lateinit var mediaPlayer: SimpleExoPlayer
    private lateinit var dataSourceFactory: DefaultDataSourceFactory

    private val mViewModel: SongDetailViewModel by lazy {
        ViewModelProvider(
            this,
            Utility.getViewModelFactory(
                SongDetailViewModel::class.java,
                SongDetailViewModel(
                    intent?.getStringExtra(SONG_ID)!!,
                    EncoraApp.appContainer.appDatabase
                ),
            )
        ).get(SongDetailViewModel::class.java)
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
        mBinding.viewCover.setOnClickListener {
            toggleSongPlay()
        }
        mBinding.ivButtonPlayPause.setOnClickListener {
            toggleSongPlay()
        }
    }

    private fun toggleSongPlay() {
        mediaPlayer.playWhenReady = mediaPlayer.playWhenReady.not()
    }

    private fun initMediaPlayer() {
        mediaPlayer = ExoPlayerFactory.newSimpleInstance(this).apply {
            repeatMode = Player.REPEAT_MODE_OFF
        }
        dataSourceFactory = DefaultDataSourceFactory(
            this,
            applicationContext.packageName
        )
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mViewModel.songItem.playLink?.toUri())
        mediaPlayer.prepare(mediaSource)

        mediaPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                mBinding.ivButtonPlayPause.setImageResource(if (playWhenReady) R.drawable.exo_controls_pause else R.drawable.exo_controls_play)
            }
        })
        mBinding.ivButtonPlayPause.setImageResource(R.drawable.exo_controls_play)
    }

    private fun releaseMediaPlayer() {
        if (::mediaPlayer.isInitialized) {
            mBinding.ivButtonPlayPause.setImageResource(R.drawable.exo_controls_play)
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    override fun onResume() {
        super.onResume()
        initMediaPlayer()
    }

    override fun onPause() {
        super.onPause()
        if (::mediaPlayer.isInitialized && mediaPlayer.playWhenReady) {
            mediaPlayer.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMediaPlayer()
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