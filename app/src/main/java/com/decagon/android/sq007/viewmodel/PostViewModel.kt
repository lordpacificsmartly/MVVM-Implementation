package com.decagon.android.sq007.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decagon.android.sq007.model.data.CommentModel
import com.decagon.android.sq007.model.data.CommentModelX
import com.decagon.android.sq007.model.data.PostModel
import com.decagon.android.sq007.model.data.PostModelX
import com.decagon.android.sq007.repository.PostRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class PostViewModel(private val repository: PostRepository) : ViewModel() {

    // instantiate mutable live data for post
    private var _allPosts: MutableLiveData<Response<PostModelX>> = MutableLiveData()
    val allPosts: LiveData<Response<PostModelX>>
        get() = _allPosts

    // instantiate mutable live data for comments
    private var _allComments: MutableLiveData<Response<CommentModelX>> = MutableLiveData()
    val allComments: LiveData<Response<CommentModelX>>
        get() = _allComments

    // instantiate mutable live data for add post
    private var _addedPost: MutableLiveData<Response<PostModel>> = MutableLiveData()
    val addedPost: LiveData<Response<PostModel>>
        get() = _addedPost

    // instantiate mutable live data for add comment
    private var _addedComment: MutableLiveData<Response<CommentModel>> = MutableLiveData()
    val addedComment: LiveData<Response<CommentModel>>
        get() = _addedComment

    fun getPosts() {
        viewModelScope.launch {
            val response = repository.getPosts()
            _allPosts.value = response
        }
    }

    fun getCommentById(userId: Int) {
        viewModelScope.launch {
            val response = repository.getCommentById(userId)
            _allComments.value = response
        }
    }

    fun addNewPost(post: PostModel) {
        viewModelScope.launch {
            val response = repository.addNewPost(post)
            _addedPost.value = response
        }
    }

    fun addNewComment(id: Int, comment: CommentModel) {
        viewModelScope.launch {
            val response = repository.addNewComment(id, comment)
            _addedComment.value = response
        }
    }
}
