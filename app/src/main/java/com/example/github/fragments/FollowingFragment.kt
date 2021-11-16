package com.example.github.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.github.adapters.FollowAdapter
import com.example.github.databinding.FragmentFollowBinding
import com.example.github.responses.FollowResponse
import com.example.github.services.ApiConfig
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback

class FollowingFragment(username: String) : Fragment() {
    private val username = username
    private lateinit var rv_user : RecyclerView
    private lateinit var shimmer: ShimmerFrameLayout

    private lateinit var binding: FragmentFollowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_user = binding.rvUser
        shimmer = binding.included.shimmerViewContainer
        findFollower(username)


    }

    private fun findFollower(query: String){
        //start loading
        loading(true)


        val client =  ApiConfig.getApiService().getFollowing("token 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",query)
        client.enqueue(object : Callback<List<FollowResponse>> {


            override fun onResponse(
                call: Call<List<FollowResponse>>,
                response: retrofit2.Response<List<FollowResponse>>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful&&responseBody!= null){
                    loading(false)
                    rv_user.visibility = View.VISIBLE
                    setReviewData(responseBody)

                }else{
                    loading(false)
                    Toast.makeText(context,"Gagal Memuat Data", Toast.LENGTH_SHORT).show()
                    Log.e(ContentValues.TAG,"onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowResponse>>, t: Throwable) {
                Toast.makeText(context,"Gagal Memuat Data", Toast.LENGTH_SHORT).show()
                Log.e(ContentValues.TAG,"onFailure: ${t.message}")
            }
        })
    }

    private fun loading(b: Boolean) {
        if (b){
            rv_user.visibility = View.GONE
            shimmer.startShimmer()
            shimmer.visibility  = View.VISIBLE
        }else{
            shimmer.stopShimmer()
            shimmer.visibility  = View.GONE
            rv_user.visibility = View.VISIBLE
        }
    }

    private fun setReviewData(items: List<FollowResponse>) {


        rv_user.layoutManager = LinearLayoutManager(context)
        val followAdapter = FollowAdapter(items)
        rv_user.adapter =followAdapter

    }


}