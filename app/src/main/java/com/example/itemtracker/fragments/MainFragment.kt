package com.example.itemtracker.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.itemtracker.MainActivity
import com.example.itemtracker.Post
import com.example.itemtracker.PostAdapter
import com.example.itemtracker.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery


open class MainFragment : Fragment() {

    lateinit var rvPosts : RecyclerView
    lateinit var rvAdapter : PostAdapter

    var allPosts: MutableList<Post> = mutableListOf()

    lateinit var swipeContainer: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pull to refresh
        swipeContainer = view.findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            Log.i(TAG, "Refreshing timeline")
            allPosts.clear()
            queryPosts()
            swipeContainer.isRefreshing = false
        }
        // Configure the refreshing color
        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        // Set up our views and click listeners
        rvPosts = view.findViewById(R.id.rvPosts)

        rvAdapter = PostAdapter(requireContext(), allPosts)
        rvPosts.adapter = rvAdapter

        // Set layout manager on RecyclerView
        rvPosts.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }

    open fun queryPosts() {
        val query : ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        query.include(Post.KEY_USER)

        query.addDescendingOrder("createdAt")
        query.limit = 20

        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    // something went wrong
                    Log.e(TAG, "Error fetching post");
                } else {
                    if (posts != null) {
                        for (post in posts) {
                            Log.i(MainActivity.TAG, "Post: " + post.getDescription() + " , username: " + post.getUser()?.username)
                        }

                        allPosts.addAll(posts)
                        rvAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "MainFragment"
    }
}