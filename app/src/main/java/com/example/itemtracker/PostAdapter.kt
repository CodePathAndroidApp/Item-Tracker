package com.example.itemtracker

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.util.*

class PostAdapter(
    private val context: Context,
    private val posts: MutableList<Post>)
    : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.itemView.findViewById<Button>(R.id.btnDelete).setOnClickListener{
            post.deleteInBackground()
            notifyDataSetChanged()
        }
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun removeItem(position: Int) {
        posts.removeAt(position)
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        //use for displaying time stamp(not yet implemented)
        val tvTimeStamp = itemView.findViewById<TextView>(R.id.tvTimeStamp)

        fun bind(post : Post) {
            if(post.getUser() != null) {
                tvDescription.text = itemView.context.getString(R.string.description, post.getDescription())
                tvDescription.setTextColor(Color.parseColor("#000000"))
            }
            else {
                tvDescription.text = itemView.context.getString(R.string.description, "(User not found)")
                tvDescription.setTextColor(Color.parseColor("#FF0000"))
            }
            tvLocation.text = itemView.context.getString(R.string.location, post.getLostlocation())

            //set time stamp
            val currentTime = System.currentTimeMillis()
            val diff : Long = (currentTime - post.createdAt.time) / (1000 * 60 * 60 * 24)
            val then = Calendar.getInstance()
            then.time = post.createdAt
            val createTime = then.get(Calendar.DAY_OF_MONTH).toString() + " " +
                    then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) +
                    " " +  then.get(Calendar.YEAR).toString()

            tvTimeStamp.text = itemView.context.getString(
                R.string.time_stamp,
                createTime,
                diff
            )

            //load the image
            Glide.with(itemView.context)
                .load(post.getImage()?.url)
                .override(150, 200)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        ivImage.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        ivImage.visibility = View.VISIBLE
                        return false
                    }
                })
                .into(ivImage)
        }
    }
}