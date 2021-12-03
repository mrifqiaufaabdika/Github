package com.example.github.activites

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.github.R
import com.example.github.adapters.SectionPagerAdapter
import com.example.github.database.UserFavorite
import com.example.github.databinding.ActivityDetailUserBinding
import com.example.github.datastore.SettingPreferences
import com.example.github.responses.DetailUserResponse
import com.example.github.services.ApiConfig
import com.example.github.ui.insert.UserFavoriteAddViewModel
import com.example.github.ui.main.MainViewModel
import com.example.github.ui.main.ViewModelFactory
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicInteger

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailUserActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var shimmer: ShimmerFrameLayout
    private lateinit var mIclove: ImageView


    private lateinit var user: DetailUserResponse

    private lateinit var userFavoriteAddViewModel: UserFavoriteAddViewModel

    private var userFavorite: UserFavorite? = null
    private var fcount = AtomicInteger()
    private var fav : Int = 0

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text1,
            R.string.tab_text2
        )
        const val EXTRA_USER_FAVORITE = "extra_user_favorit"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")
        shimmer = binding.included.shimmerViewContainer
        mIclove = binding.icLove

        userFavoriteAddViewModel = obtainViewModel(this)

        if (username != null) {
            getDetailUser(username)

            val sectionsPagerAdapter = SectionPagerAdapter(this, username)
            val viewPager: ViewPager2 = binding.viewPager
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = binding.tabLayout
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }


        val t = Thread {
            val num: Int = userFavoriteAddViewModel.getOne(username.toString())
            fcount.set(num)
        }
        t.priority = 10
        t.start()
        t.join()
        fav = fcount.get()


        if ( fav == 0) {
            mIclove.setImageDrawable(this.getDrawable(R.drawable.ic_favorite))
        }else{
            mIclove.setImageDrawable(this.getDrawable(R.drawable.ic_favorite_active))
        }

        mIclove.setOnClickListener(View.OnClickListener {

            if (user != null && fav == 0) {
                userFavorite = UserFavorite()
                userFavorite.let { userFavorite ->
                    userFavorite?.login = user.login
                    userFavorite?.avatarUrl = user.avatarUrl
                }

                userFavoriteAddViewModel.insert(userFavorite as UserFavorite)
                mIclove.setImageDrawable(this.getDrawable(R.drawable.ic_favorite_active))
                fav++
                showToast("Berhasi Menambahkan Favorit")
            }else{
                showToast("Sudah Favorit")
            }

        })


    }

    private fun obtainViewModel(activity: AppCompatActivity): UserFavoriteAddViewModel {
        var pref = SettingPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory).get(UserFavoriteAddViewModel::class.java)
    }



    private fun getDetailUser(username: String) {
        loading(true)

        val client = ApiConfig.getApiService()
            .getDetailUser(this.getString(R.string.github_key_api), username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                loading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    user = responseBody
                    initData(user)



                } else {
                    showToast("Gagal Memuat Detail User")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                loading(false)
                showToast("Gagal Memuat Detail User")
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
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
        binding.company.text = user.company + " | "
        binding.location.text = user.location
        binding.follower.text = user.followers.toString() + "\nFollower"
        binding.repo.text = user.publicRepos.toString() + "\nRepository"
        binding.folowing.text = user.following.toString() + "\nFollowing"


    }

    private fun loading(b: Boolean) {
        if (b) {

            shimmer.startShimmer()
            shimmer.visibility = View.VISIBLE
        } else {
            shimmer.stopShimmer()
            shimmer.visibility = View.GONE

        }


    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}