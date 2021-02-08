package com.hariagus.staterproject.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hariagus.staterproject.core.R
import com.hariagus.staterproject.core.databinding.ItemListBinding
import com.hariagus.staterproject.core.domain.model.Movie

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var listData = ArrayList<Movie>()
    var onItemClick: ((Movie) -> Unit)? = null

    fun setData(newListData: List<Movie>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): MovieViewHolder {
        val itemListBinding = ItemListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieViewHolder(itemListBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    inner class MovieViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding) {
                tvTitle.text = movie.title
                tvScore.text = movie.voteAverage.toString()
                Glide.with(itemView.context)
                    .load(itemView.context.getString(R.string.url_poster, movie.posterPath))
                    .apply {
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.loading_animation)
                    }
                    .into(roundedPoster)
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}