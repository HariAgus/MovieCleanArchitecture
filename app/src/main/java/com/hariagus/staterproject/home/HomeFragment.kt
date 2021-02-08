package com.hariagus.staterproject.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hariagus.staterproject.core.data.source.Resource
import com.hariagus.staterproject.core.domain.model.Movie
import com.hariagus.staterproject.core.ui.MovieAdapter
import com.hariagus.staterproject.core.utils.SortUtils
import com.hariagus.staterproject.databinding.FragmentHomeBinding
import com.hariagus.staterproject.detail.DetailActivity
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _fragmentHomeBinding: FragmentHomeBinding? = null
    private val binding get() = _fragmentHomeBinding

    private val viewModel: HomeViewModel by viewModel()
    private val moviesAdapter: MovieAdapter by lazy { MovieAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setList(SortUtils.NEWEST)

        moviesAdapter.onItemClick = { selectedData ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DATA, selectedData)
            startActivity(intent)
        }

        binding?.progressSpinKitList?.visibility = View.VISIBLE
        with(binding?.rvMovie) {
            this?.setHasFixedSize(true)
            this?.adapter = moviesAdapter
        }

        binding?.fabNewest?.setOnClickListener { setList(SortUtils.NEWEST) }
        binding?.fabOldest?.setOnClickListener { setList(SortUtils.OLDEST) }
        binding?.fabPopularity?.setOnClickListener { setList(SortUtils.POPULARITY) }
    }

    private fun setList(sort: String) {
        viewModel.getMovies(sort).observe(viewLifecycleOwner, movieObserver)
    }

    private val movieObserver = Observer<Resource<List<Movie>>> { movies ->
        if (movies != null) {
            when (movies) {
                is Resource.Loading -> binding?.progressSpinKitList?.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding?.progressSpinKitList?.visibility = View.GONE
                    moviesAdapter.setData(movies.data)
                    moviesAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    binding?.progressSpinKitList?.visibility = View.GONE
                    Toast.makeText(context, "There is an Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentHomeBinding = null
    }
}