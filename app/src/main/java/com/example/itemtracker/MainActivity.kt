package com.example.itemtracker

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.parse.*
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            val description = findViewById<EditText>(R.id.description).text.toString()
            val lostlocation = findViewById<EditText>(R.id.lostlocation).text.toString()
            val user = ParseUser.getCurrentUser()
            submitPost(description, lostlocation ,user)
        }

        findViewById<Button>(R.id.btnTakePicture).setOnClickListener {
            // launch camera
        }

        queryPost()
    }

    fun submitPost(description: String, lostlocation : String, user: ParseUser) {
        val post = Post()
        post.setDescription(description)
        post.setLostDescription(lostlocation)
        post.setUser(user)
        post.saveInBackground { exception ->
            if (exception != null) {
                // Something has went wrong
                Log.e(TAG, "Error while saving post")
                exception.printStackTrace()
                Toast.makeText(this, "Error in saving post", Toast.LENGTH_SHORT).show()
            } else {
                Log.i(TAG, "Successfully saved post")
            }
        }
    }

    fun queryPost() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        // Find all Post objects
        query.include(Post.KEY_USER)
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "Error fetching posts")
                    } else {
                        if (posts != null) {
                             for (post in posts) {
                                Log.i(TAG, "Post: " + post.getDescription() + "lost at: " + post.getLostlocation()
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