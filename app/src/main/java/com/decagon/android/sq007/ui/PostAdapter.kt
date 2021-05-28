package com.decagon.android.sq007.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.databinding.ItemPostBinding
import com.decagon.android.sq007.model.data.PostModel
import com.decagon.android.sq007.model.data.PostModelX
import com.decagon.android.sq007.utils.PostClickListener
import java.util.* // ktlint-disable no-wildcard-imports
import kotlin.collections.ArrayList

class PostAdapter(private val postClickListener: PostClickListener) : RecyclerView.Adapter<PostAdapter.PostViewHolder>(), Filterable {

    private var post = arrayListOf<PostModel>()

    private var initialPost = arrayListOf<PostModel>()

    inner class PostViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root)

//    private val diffCallback = object : DiffUtil.ItemCallback<PostModelX>() {
//        override fun areItemsTheSame(oldItem: PostModelX, newItem: PostModelX): Boolean {
//            return oldItem == newItem
//        }
//
//        override fun areContentsTheSame(oldItem: PostModelX, newItem: PostModelX): Boolean {
//            return oldItem == newItem
//        }
//    }
//    private val differ = AsyncListDiffer(this, diffCallback)
//    private var post: PostModelX
//        get() = differ.currentList
//        set(value) { differ.submitList(value) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.binding.apply {
            val postX = post[position]
            tvTitle.text = postX.title
            tvBody.text = postX.body
            postLayout.setOnClickListener {
                val postId = post[position].id
                Log.d("PostAdapter", "onBindViewHolder: ${post[position].id}")
                postId?.let { postClickListener.onPostClick(it) }
            }
        }
    }

    override fun getItemCount() = post.size

    fun setUpPost(allPost: PostModelX) {
        this.post = allPost
        this.initialPost = allPost
        notifyDataSetChanged()
    }

    fun addNewPost(newPost: PostModel) {
        post.add(0, newPost)
        notifyItemInserted(0)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchString = constraint.toString()
                if (searchString.isEmpty()) {
                    post = initialPost
                } else {
                    post = initialPost
                    val temporaryPost = arrayListOf<PostModel>()
                    for (post in post) {
                        if (post.title.toLowerCase(Locale.ROOT).trim().contains(searchString)) {
                            temporaryPost.add(post)
                        }
                    }
                    post = temporaryPost
                }
                val filterPosts = FilterResults()
                filterPosts.values = post
                return filterPosts
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                post = results?.values as ArrayList<PostModel>
                notifyDataSetChanged()
            }
        }
    }
}
