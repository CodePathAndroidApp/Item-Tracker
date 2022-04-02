package com.example.itemtracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itemtracker.Post
import com.example.itemtracker.PostAdapter
import com.example.itemtracker.R
import com.parse.ParseQuery


class MainFragment : Fragment() {

    private lateinit var rvPosts : RecyclerView

    private lateinit var rvAdapter : PostAdapter


    private var posts = mutableListOf<Post>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPosts = view.findViewById(R.id.rvPosts)

        rvAdapter = PostAdapter(requireContext(), posts)
        rvPosts.adapter = rvAdapter

        rvPosts.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }

    private fun queryPosts() {
        val query : ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        query.include(Post.KEY_USER)

        query.addDescendingOrder("createdAt")
        query.limit = 20

        query.findInBackground { returnedPosts, e ->
            if(e != null) {
                e.printStackTrace()
            }
            else {
                if(returnedPosts != null) {
                    posts.addAll(returnedPosts)
                    rvAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}