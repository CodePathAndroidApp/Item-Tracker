package com.example.itemtracker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.itemtracker.fragments.ComposeFragment
import com.example.itemtracker.fragments.ProfileFragment
import com.example.itemtracker.fragments.MainFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import java.io.File

class MainActivity : AppCompatActivity() {

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            item ->

            var fragmentToShow: Fragment? = null
            when (item.itemId) {
                R.id.action_main -> {
                    fragmentToShow = MainFragment()
                }
                R.id.action_compose -> {
                    fragmentToShow = ComposeFragment()
                }
                R.id.action_profile -> {
                    fragmentToShow = ProfileFragment()
                }
            }

            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
            }

            true
        }

        // Set default menu item selection of BottomNavigationView item; set which fragment is shown by default
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_main

    }

    fun queryPosts() {
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        // Return the Posts in descending order based on time created; i.e. newer posts appear first
        query.addDescendingOrder("createdAt")
        // Find all Post objects in server
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "Error fetching posts")
                } else {
                    if(posts != null) {
                        for (post in posts) {
                            Log.i(
                                MainActivity.TAG, "Post: " + post.getDescription() + "lost at: " + post.getLostlocation()
                                        + " , username: " + post.getUser()?.username)
                        }
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "Mainactivity"
    }
}