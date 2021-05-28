package com.decagon.android.sq007.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.decagon.android.sq007.R
import com.decagon.android.sq007.databinding.ActivityCommentBinding
import com.decagon.android.sq007.model.data.CommentModel
import com.decagon.android.sq007.model.network.NetworkBuilder
import com.decagon.android.sq007.repository.PostRepository
import com.decagon.android.sq007.utils.POST_ID
import com.decagon.android.sq007.viewmodel.PostViewModel
import com.decagon.android.sq007.viewmodel.ViewModelFactory

class CommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentBinding

    // declaring variable
    private lateinit var viewModel: PostViewModel

    private lateinit var commentAdapter : CommentAdapter
    private var postId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        commentAdapter = CommentAdapter()
        binding.rvComment.adapter = commentAdapter

        binding.floatingActionButton.setOnClickListener {
            showAlertDialogButtonClicked(binding.root)
        }

        postId = intent.getIntExtra(POST_ID, 0)

        val commentRepository = PostRepository(NetworkBuilder.getPostEndPoint())
        val viewModelFactory = ViewModelFactory(commentRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PostViewModel::class.java)
        viewModel.getCommentById(postId)

        viewModel.addedComment.observe(this,{
            if (it.isSuccessful) {
                it.body()?.let { it1 -> commentAdapter.addNewComment(it1) }
            }
        })
        viewModel.allComments.observe(
            this,
            Observer { response ->
                if (response.isSuccessful) {
                    binding.rvComment.layoutManager = LinearLayoutManager(this)
                    response.body()?.let { commentAdapter.setUpComments(it) }

                } else {
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun showAlertDialogButtonClicked(view: View?) {

        // Create an alert builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Comment")

        // set the custom layout
        val customLayout: View = layoutInflater.inflate(
            R.layout.fragment_add_comment, null
        )
        builder.setView(customLayout)

        // add a button
        builder.setPositiveButton(
            "OK"
        ) { dialog, which ->
            val edtCommentBody: EditText = customLayout.findViewById(R.id.edit_text_body)
            val edtCommentId: EditText = customLayout.findViewById(R.id.edit_text_id)
            val edtCommentEmail: EditText = customLayout.findViewById(R.id.edit_text_email)
            val edtCommentName: EditText = customLayout.findViewById(R.id.edit_text_name)
            sendDialogDataToActivity(edtCommentBody.text.toString(), edtCommentId.text.toString().toInt(), edtCommentEmail.text.toString(), edtCommentName.text.toString())
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun sendDialogDataToActivity(
        commentBody: String,
        commentId: Int,
        commentEmail: String,
        commentName: String
    ) {
        Log.d("MainActivity", "sendDialogDataToActivity: $commentBody")
        val comment = CommentModel(postId = commentId, body = commentBody, name = commentName, email = commentEmail)
        viewModel.addNewComment(postId, comment)
    }
}
