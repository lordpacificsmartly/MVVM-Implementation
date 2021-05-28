package com.decagon.android.sq007.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.decagon.android.sq007.R
import com.decagon.android.sq007.databinding.ActivityMainBinding
import com.decagon.android.sq007.model.data.PostModel
import com.decagon.android.sq007.model.network.NetworkBuilder
import com.decagon.android.sq007.repository.PostRepository
import com.decagon.android.sq007.utils.POST_ID
import com.decagon.android.sq007.utils.PostClickListener
import com.decagon.android.sq007.viewmodel.PostViewModel
import com.decagon.android.sq007.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity(), PostClickListener {
    // view binding vaiable
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchView: SearchView

    // declaring variable
    private lateinit var viewModel: PostViewModel

    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // initialize the post adapter
        postAdapter = PostAdapter(this)

        binding.floatingActionButton.setOnClickListener {
            showAlertDialogButtonClicked(binding.root)
        }

        // get instance of repository with that of the network builder
        val repository = PostRepository(NetworkBuilder.getPostEndPoint())
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PostViewModel::class.java)
        viewModel.getPosts()

        viewModel.addedPost.observe(
            this,
            {
                if (it.isSuccessful) {
                    it.body()?.let { it1 -> postAdapter.addNewPost(it1) }
                }
            }
        )
        viewModel.allPosts.observe(
            this,
            // observe response
            Observer { response ->
                if (response.isSuccessful) {
                    binding.rvPost.layoutManager = LinearLayoutManager(this)
                    response.body()?.let { allPost ->

                        postAdapter.setUpPost(allPost)
                    }
                    binding.rvPost.adapter = postAdapter
                } else {
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    // inflate menu and create search action
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchAction = menu?.findItem(R.id.action_search) // find search id and pass it to the val
        // check if searchAction is not null
        if (searchAction != null) {
            searchView = searchAction.actionView as SearchView // set searchView as SearchView
        }

        // set setOnQueryTextListener and implement object members
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                postAdapter.filter.filter(newText)
                return true
            }
        })
        return true
    }

    override fun onPostClick(postId: Int) {
        val intent = Intent(this, CommentActivity::class.java)
        intent.putExtra(POST_ID, postId)
        startActivity(intent)
    }

    private fun showAlertDialogButtonClicked(view: View?) {

        // Create an alert builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Post")

        // set the custom layout
        val customLayout: View = layoutInflater.inflate(
            R.layout.fragment_add_post, null
        )
        builder.setView(customLayout)

        // add a button
        builder.setPositiveButton(
            "OK"
        ) { dialog, which ->
            val edtPostTitle: EditText = customLayout.findViewById(R.id.edit_text_title)
            val edtPostBody: EditText = customLayout.findViewById(R.id.edit_text_body)
            val edtPostId: EditText = customLayout.findViewById(R.id.edit_text_id)
            sendDialogDataToActivity(edtPostTitle.text.toString(), edtPostBody.text.toString(), edtPostId.text.toString().toInt())
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /** This function takes the needed parameters to be retrieved from the user and send same to the activity */
    private fun sendDialogDataToActivity(postTitle: String, postBody: String, postId: Int) {
        Log.d("MainActivity", "sendDialogDataToActivity: $postId $postBody $postTitle")
        val post = PostModel(postBody, postId, postTitle)
        viewModel.addNewPost(post)
    }
}
