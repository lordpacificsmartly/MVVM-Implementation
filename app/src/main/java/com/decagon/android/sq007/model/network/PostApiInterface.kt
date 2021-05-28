package com.decagon.android.sq007.model.network

import com.decagon.android.sq007.model.data.CommentModel
import com.decagon.android.sq007.model.data.CommentModelX
import com.decagon.android.sq007.model.data.PostModel
import com.decagon.android.sq007.model.data.PostModelX
import retrofit2.Response
import retrofit2.http.* // ktlint-disable no-wildcard-imports

interface PostApiInterface {

    // get post method
    @GET("posts")
    suspend fun getPosts(): Response<PostModelX>

    // get comment method
    @GET("comments")
    suspend fun getCommentById(@Query("postId") postIdd: Int): Response<CommentModelX>

    // add post
    @POST("posts")
    suspend fun addPost(@Body post: PostModel): Response<PostModel>

    // add comment
    @POST("posts/{id}/comments")
    suspend fun addComment(@Path("id") id: Int, @Body comment: CommentModel): Response<CommentModel>
}
