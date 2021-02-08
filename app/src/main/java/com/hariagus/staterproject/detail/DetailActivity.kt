package com.hariagus.staterproject.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hariagus.staterproject.R
import com.hariagus.staterproject.core.domain.model.Movie
import com.hariagus.staterproject.databinding.ActivityDetailBinding
import org.koin.android.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailMovie = intent.getParcelableExtra<Movie>(EXTRA_DATA)
        if (detailMovie != null) {
            loadDataDetail(detailMovie)
        }
    }

    private fun loadDataDetail(movie: Movie) {
        with(binding) {
            tvTitleDetail.text = movie.title
            tvLanguage.text = movie.originalLanguage
            tvPopularity.text = movie.popularity.toString()
            tvOverview.text = movie.overview
            tvReleaseDate.text = movie.releaseDate
            tvScoreDetail.text = movie.voteAverage.toString()

            /**
             * Poster Detail
             */
            Glide.with(this@DetailActivity)
                .load(this@DetailActivity.getString(R.string.url_poster, movie.posterPath))
                .apply {
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.loading_animation)
                }
                .into(roundedPosterDetail)

            /**
             * Background Detail
             */
            Glide.with(this@DetailActivity)
                .load(this@DetailActivity.getString(R.string.url_poster, movie.backdropPath))
                .apply {
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.loading_animation)
                }
                .into(posterBg)

            var statusFavorite = movie.isFavorite
            setFavorite(statusFavorite)
            binding.fabFavorite.setOnClickListener {
                statusFavorite = !statusFavorite
                viewModel.setFavoriteMovie(movie, statusFavorite)
                setFavorite(statusFavorite)
            }
        }
    }

    private fun setFavorite(state: Boolean) {
        if (state) {
            binding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.ic_favorite_primary
                )
            )
        } else {
            binding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.ic_favorite_border
                )
            )
        }
    }
}