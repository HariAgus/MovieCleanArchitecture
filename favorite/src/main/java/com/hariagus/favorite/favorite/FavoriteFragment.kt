package com.hariagus.favorite.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hariagus.favorite.DataStateFavorite
import com.hariagus.favorite.databinding.FragmentFavoriteBinding
import com.hariagus.favorite.di.favoriteModule
import com.hariagus.staterproject.core.ui.MovieAdapter
import com.hariagus.staterproject.detail.DetailActivity
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules


class FavoriteFragment : Fragment() {

    private var _favoriteBinding: FragmentFavoriteBinding? = null
    private val binding get() = _favoriteBinding

    private val movieAdapter: MovieAdapter by lazy { MovieAdapter() }
    private val viewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _favoriteBinding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadKoinModules(favoriteModule)

        binding?.progressSpinKitList?.visibility = View.VISIBLE
        viewModel.getFavoriteMovie().observe(viewLifecycleOwner, { movies ->
            binding?.progressSpinKitList?.visibility =View.GONE
            if (movies.isNullOrEmpty()) {
                setDataState(DataStateFavorite.BLANK)
            } else {
                setDataState(DataStateFavorite.SUCCESS)
            }
            movieAdapter.setData(movies)
        })

        movieAdapter.onItemClick = { selectedData ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DATA, selectedData)
            startActivity(intent)
        }

        with(binding?.rvMovieFavorite) {
            this?.setHasFixedSize(true)
            this?.adapter = movieAdapter
        }
    }

    private fun setDataState(state: DataStateFavorite) {
        when (state) {
            DataStateFavorite.BLANK -> {
                binding?.lottieNotFound?.visibility = View.VISIBLE
            }
            DataStateFavorite.SUCCESS -> {
                binding?.lottieNotFound?.visibility = View.GONE
            }
        }
    }
}