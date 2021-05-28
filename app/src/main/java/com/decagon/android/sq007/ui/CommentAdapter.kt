package com.decagon.android.sq007.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.databinding.ItemCommentBinding
import com.decagon.android.sq007.model.data.CommentModel
import com.decagon.android.sq007.model.data.CommentModelX

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private var comment = arrayListOf<CommentModel>()

    inner class CommentViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.binding.apply {
            val comment = comment[position]
            tvId.text = comment.id.toString()
            tvName.text = comment.name
            tvEmail.text = comment.email
            tvBody.text = comment.body
        }
    }

    override fun getItemCount() = comment.size

    fun setUpComments(allcoment: CommentModelX) {
        this.comment = allcoment
        notifyDataSetChanged()
    }

    fun addNewComment(newComment : CommentModel){
        comment.add(0, newComment)
        notifyItemInserted(0)
    }
}
