package com.example.github.activites

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.github.R
import com.example.github.adapters.SectionPagerAdapter
import com.example.github.databinding.ActivityDetailUserBinding
import com.example.github.responses.DetailUserResponse
import com.example.github.services.ApiConfig
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var shimmer: ShimmerFrameLayout

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text1,
            R.string.tab_text2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")
        shimmer = binding.included.shimmerViewContainer

        if (username != null) {
            getDetailUser(username)

            val sectionsPagerAdapter = SectionPagerAdapter(this, username)
            val viewPager : ViewPager2 = binding.viewPager
            viewPager.adapter = sectionsPagerAdapter
            val tabs :TabLayout = binding.tabLayout
            TabLayoutMediator(tabs, viewPager) {
                    tab,position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }




    }

    private fun getDetailUser(username: String) {
        loading(true)

        val client = ApiConfig.getApiService().getDetailUser("token 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",username)
        client.enqueue(object : Callback<DetailUserResponse>{
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                loading(false)
                val responseBody = response.body()
                if (response.isSuccessful&&responseBody!= null) {
                    initData(responseBody)
                }else{
                    Toast.makeText(this@DetailUserActivity,"Gagal Memuat Detail User",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                loading(false)
                Toast.makeText(this@DetailUserActivity,"Gagal Memuat Detail User",Toast.LENGTH_SHORT).show()
                Log.e(ContentValues.TAG,"onFailure: ${t.message}")
            }

        })




    }

    private fun initData(user: DetailUserResponse) {
        Glide.with(this)
            .load(user.avatarUrl)
            .circleCrop()
            .into(binding.avatar)
        binding.name.text = user.name
        binding.username.text = user.login
        binding.company.text = user.company+" | "
        binding.location.text = user.location
        binding.follower.text = user.followers.toString()+"\nFollower"
        binding.repo.text = user.publicRepos.toString()+"\nRepository"
        binding.folowing.text = user.following.toString()+"\nFollowing"


    }

    private fun loading(b: Boolean) {
        if (b){

            shimmer.startShimmer()
            shimmer.visibility  = View.VISIBLE
        }else{
            shimmer.stopShimmer()
            shimmer.visibility  = View.GONE
            
        }
    }
}