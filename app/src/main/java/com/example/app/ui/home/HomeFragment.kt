package com.example.app.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.Myadapter
import com.example.app.R
import com.example.app.User
import com.example.app.api
import com.example.app.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

lateinit var adapter: Myadapter
lateinit var recycler: RecyclerView
var mode_code: Int? = null

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.rv)


        val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightMode == Configuration.UI_MODE_NIGHT_NO) {
            mode_code = 0
            val fit = Retrofit.Builder()
                .baseUrl("https://dummyjson.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(api::class.java)

            val data = fit.getUserdata()
            data.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    var body = response.body()
                    var userdata = body?.users!!


                    adapter = Myadapter(this@HomeFragment, userdata, mode_code.toString())
                    recycler.adapter = adapter
                    val layoutManager = LinearLayoutManager(context)
                    recycler.layoutManager = layoutManager

                    shareManipulation()
                    likeManipulation()


                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("Main", "onFailure: ${t.message} ")
                }

            })
        } else {
            mode_code = 1

            val fit = Retrofit.Builder()
                .baseUrl("https://dummyjson.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(api::class.java)

            val data = fit.getUserdata()
            data.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    var body = response.body()
                    var userdata = body?.users!!

                    adapter = Myadapter(this@HomeFragment, userdata, mode_code.toString())
                    recycler.adapter = adapter
                    val layoutManager = LinearLayoutManager(context)
                    recycler.layoutManager = layoutManager

                    likeManipulation()
                    shareManipulation()
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("Main", "onFailure: ${t.message} ")
                }

            })
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun shareManipulation() {
        adapter.setOnItemClickListener(object : Myadapter.onItemClickListener {
            override fun onItemClick(position: Int, msg: String, author: String, share: TextView) {
//                            Toast.makeText(HomeFragment, "one", Toast.LENGTH_SHORT).show()
                Log.d("one", "onItemClick: $msg")
                val message: String = "$author \n$msg"
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_TEXT, message)
                shareIntent.type = "text/plain"

                val intent = Intent.createChooser(shareIntent, null)


                startActivity(intent)
                var shareCount = share.text.toString().toInt() + 1
                share.text = shareCount.toString()
            }


        })
    }

    fun likeManipulation() {
        adapter.onLikeClickListener(object : Myadapter.onLikeListener {

            override fun onLikeClick(position: Int, view: ImageButton, like: TextView) {
                Log.d("qwerty", "onLikeClick: ")
                val id = view.tag
                if (id == "unfill") {
                    view.setImageResource(R.drawable.heart_fill)
                    view.tag = "fill"
                    val likeCount = like.text.toString().toInt() + 1
                    like.text = likeCount.toString()
                } else {
                    view.setImageResource(R.drawable.heart)
                    view.tag = "unfill"
                    val likeCount = like.text.toString().toInt() - 1
                    like.text = likeCount.toString()
                }
            }
        })
    }
}