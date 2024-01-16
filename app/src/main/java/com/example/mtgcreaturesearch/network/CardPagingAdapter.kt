//package com.example.mtgcreaturesearch.network;
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.paging.PagingDataAdapter
//import androidx.recyclerview.widget.ListAdapter
//import com.example.mtgcreaturesearch.Model.Data
//
////
//
//class MovieListAdapter : PagingDataAdapter<Data, MoviePosterViewHolder>(MovieDiffCallBack()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePosterViewHolder {
//        return MoviePosterViewHolder(
//            ItemMoviePosterBinding.inflate(
//                LayoutInflater.from(parent.context), parent, false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: MoviePosterViewHolder, position: Int) {
//        holder.bind(getItem(position)?.image)
//    }
//}
//
//class MovieDiffCallBack : DiffUtil.ItemCallback<MovieUi>() {
//    override fun areItemsTheSame(oldItem: MovieUi, newItem: MovieUi): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: MovieUi, newItem: MovieUi): Boolean {
//        return oldItem == newItem
//    }
//}
//
//class MoviePosterViewHolder(
//    val binding: ItemMoviePosterBinding
//) : RecyclerView.ViewHolder(binding.root) {
//
//    fun bind(path: String?) {
//        path?.let {
//            binding.ivMoviePoster.load("https://image.tmdb.org/t/p/w500/$it") {
//                crossfade(durationMillis = 2000)
//                transformations(RoundedCornersTransformation(12.5f))
//            }
//        }
//    }
//}