package com.example.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlin.random.Random

class Myadapter(var context: Fragment, private val user: List<UserX>, val nignt: String) :
    RecyclerView.Adapter<Myadapter.MyViewholder>() {

    lateinit var mListener: onItemClickListener
    lateinit var mLike: onLikeListener

    interface onItemClickListener {
        fun onItemClick(position: Int, msg: String, author: String, share: TextView)

    }

    interface onLikeListener {
        fun onLikeClick(position: Int, view: ImageButton, like: TextView)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    fun onLikeClickListener(listenerq: onLikeListener) {
        mLike = listenerq
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myadapter.MyViewholder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        val o = nignt.toString()
        return MyViewholder(itemview, mListener!!, o, mLike)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentposition = user[position]
        val arrayList = ArrayList<String>()


        val likecount = Random.nextInt(1, 101)
        val sharecount = Random.nextInt(1, 101)
        val commentcount = Random.nextInt(1, 101)
        repeat(30) {
            val tweet = generateRandomTweet(position)
            arrayList.add(tweet)
        }

        holder.username.text = currentposition.username
        Picasso.get().load(currentposition.image).into(holder.profile);
        holder.email.text = currentposition.email
        holder.tweet.text = arrayList[position]
        holder.like.text = likecount.toString()
        holder.share.text = sharecount.toString()
        holder.comment.text = commentcount.toString()

        if (holder.code == "0") {
            holder.cardlinear.setBackgroundResource(R.drawable.lighttweet)
        } else {
            holder.cardlinear.setBackgroundResource(R.drawable.tweet)
        }


    }

    override fun getItemCount(): Int {
        return user.size
    }

    class MyViewholder(
        itemView: View,
        listener: onItemClickListener,
        o: String,
        likeListener: onLikeListener
    ) :
        RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.itemcard)
        val username: TextView = itemView.findViewById(R.id.uname)
        val email: TextView = itemView.findViewById(R.id.email)
        val tweet: TextView = itemView.findViewById(R.id.tweet)
        val profile: ImageView = itemView.findViewById(R.id.profile_image)
        val like: TextView = itemView.findViewById(R.id.like)
        val comment: TextView = itemView.findViewById(R.id.comment)
        val sharebtn: LinearLayout = itemView.findViewById(R.id.sharebtn)
        val share: TextView = itemView.findViewById(R.id.share)
        val likebtn: LinearLayout = itemView.findViewById(R.id.likebtn)
        val cardlinear: LinearLayout = itemView.findViewById(R.id.cardLinear)
        val ib_like: ImageButton = itemView.findViewById(R.id.ib_like)
        val code = o

        init {
            sharebtn.setOnClickListener {
                listener.onItemClick(
                    adapterPosition,
                    tweet.text.toString(),
                    username.text.toString(),
                    share
                )
            }

            likebtn.setOnClickListener {
                likeListener.onLikeClick(adapterPosition, ib_like, like)

            }


        }
    }


    private fun generateRandomTweet(position: Int): String {

        val tweets = listOf(
            "I'm having a great day!",
            "Just finished reading a fantastic book.",
            "Excited about the new project I'm working on.",
            "Enjoying a delicious meal with friends.",
            "Can't wait for the weekend!",
            "Feeling motivated and ready to conquer the world!",
            "Loving the sunny weather today.",
            "Watching my favorite TV show marathon.",
            "Celebrating a milestone achievement.",
            "Reflecting on life's blessings.",
            "Dreaming big and aiming high!"
        )

        val randomIndex = Random.nextInt(tweets.size)
        return tweets[randomIndex]

    }
}