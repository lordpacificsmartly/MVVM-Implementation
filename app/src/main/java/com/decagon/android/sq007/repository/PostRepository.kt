package com.decagon.android.sq007.repository

import com.decagon.android.sq007.model.data.CommentModel
import com.decagon.android.sq007.model.data.CommentModelX
import com.decagon.android.sq007.model.data.PostModel
import com.decagon.android.sq007.model.data.PostModelX
import com.decagon.android.sq007.model.network.PostApiInterface
import retrofit2.Response

class PostRepository(private val postApiInterface: PostApiInterface) {
    suspend fun getPosts(): Response<PostModelX> {
        return postApiInterface.getPosts()
    }

    suspend fun getCommentById(userId: Int): Response<CommentModelX> {
        return postApiInterface.getCommentById(userId)
    }

    suspend fun addNewPost(post : PostModel) : Response<PostModel>{
        return postApiInterface.addPost(post)
    }

    suspend fun addNewComment(id : Int, comment : CommentModel) : Response<CommentModel>{
        return postApiInterface.addComment(id, comment)
    }
}
