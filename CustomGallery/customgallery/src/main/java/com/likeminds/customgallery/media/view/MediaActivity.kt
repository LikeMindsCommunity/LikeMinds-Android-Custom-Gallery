package com.likeminds.customgallery.media.view

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.navigation.fragment.NavHostFragment
import com.likeminds.customgallery.R
import com.likeminds.customgallery.media.model.*
import com.likeminds.customgallery.utils.customview.BaseAppCompatActivity

internal class MediaActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        Log.d("PUI", "onCreate: called")

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as? NavHostFragment ?: return
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_media_graph)
        val navController = navHostFragment.navController
        if (intent != null) {
            val bundle = intent.extras
            if (bundle != null) {
                val mediaExtras = bundle.getParcelable<MediaExtras>(BUNDLE_MEDIA_EXTRAS)
                if (mediaExtras != null) {
                    when (mediaExtras.mediaScreenType) {
                        MEDIA_EDIT_SCREEN -> {
                            initActionBar(mediaExtras, R.style.AppTheme_Black)
                            val args = Bundle().apply {
                                putParcelable(ARG_MEDIA_EXTRAS, mediaExtras)
                            }
                            navGraph.setStartDestination(R.id.mediaEditFragment)
                            navController.setGraph(navGraph, args)
                        }
                        MEDIA_CROP_SCREEN -> {
                            val args = Bundle().apply {
                                putParcelable(ARG_SINGLE_URI_DATA, mediaExtras.singleUriData)
                                putBoolean(ARG_IS_FROM_ACTIVITY, true)
                                putBoolean(ARG_CROP_SQUARE, mediaExtras.cropSquare ?: false)
                            }
                            navGraph.setStartDestination(R.id.imageCropFragment)
                            navController.setGraph(navGraph, args)
                        }
                        MEDIA_VIDEO_PLAY_SCREEN -> {
                            initActionBar(mediaExtras, R.style.AppTheme)
                            val args = Bundle().apply {
                                putParcelable(ARG_MEDIA_EXTRAS, mediaExtras)
                            }
                            navGraph.setStartDestination(R.id.playVideoFragment)
                            navController.setGraph(navGraph, args)
                        }
                        MEDIA_HORIZONTAL_LIST_SCREEN -> {
                            initActionBar(mediaExtras, R.style.AppTheme)
                            val args = Bundle().apply {
                                putParcelable(ARG_MEDIA_EXTRAS, mediaExtras)
                            }
                            navGraph.setStartDestination(R.id.mediaHorizontalListFragment)
                            navController.setGraph(navGraph, args)
                        }
                        MEDIA_DOCUMENT_SEND_SCREEN -> {
                            initActionBar(mediaExtras, R.style.AppTheme_Black)
                            val args = Bundle().apply {
                                putParcelable(ARG_MEDIA_EXTRAS, mediaExtras)
                            }
                            navGraph.setStartDestination(R.id.documentSendFragment)
                            navController.setGraph(navGraph, args)
                        }
                        MEDIA_AUDIO_EDIT_SEND_SCREEN -> {
                            initActionBar(mediaExtras, R.style.AppTheme_Black)
                            val args = Bundle().apply {
                                putParcelable(ARG_MEDIA_EXTRAS, mediaExtras)
                            }
                            navGraph.setStartDestination(R.id.audioEditSendFragment)
                            navController.setGraph(navGraph, args)
                        }
                    }
                }
            }
        }
    }

    private fun initActionBar(mediaExtras: MediaExtras, theme: Int) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = mediaExtras.title
        supportActionBar?.subtitle = mediaExtras.subtitle
        setTheme(theme)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val BUNDLE_MEDIA_EXTRAS = "BUNDLE_MEDIA_EXTRAS"
        const val ARG_MEDIA_EXTRAS = "media_extras"
        const val ARG_SINGLE_URI_DATA = "singleUriData"
        const val ARG_IS_FROM_ACTIVITY = "is_from_activity"
        const val ARG_CROP_SQUARE = "crop_square"

        @JvmStatic
        fun startActivity(context: Context, mediaExtras: MediaExtras) {
            val intent = Intent(context, MediaActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_MEDIA_EXTRAS, mediaExtras)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        @JvmStatic
        fun getIntent(
            context: Context,
            mediaExtras: MediaExtras,
            clipData: ClipData? = null,
        ): Intent {
            val intent = Intent(context, MediaActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_MEDIA_EXTRAS, mediaExtras)
            intent.putExtras(bundle)
            if (clipData != null) {
                intent.clipData = clipData
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            return intent
        }
    }
}