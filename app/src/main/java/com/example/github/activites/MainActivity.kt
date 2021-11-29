package com.example.github.activites

import android.app.SearchManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.github.*
import com.example.github.adapters.ListUserAdapter
import com.example.github.databinding.ActivityMainBinding
import com.example.github.services.ApiConfig
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rv_user:RecyclerView
    private lateinit var shimmer:ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabFavorite.setOnClickListener {
            startActivity(Intent(this@MainActivity, FavoriteActivity::class.java))
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu,menu)

        shimmer = binding.included.shimmerViewContainer
        rv_user = binding.rvUser


        val searchManager =  getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView





        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.cari_pengguna_github)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
        /*
        method ini untuk search selasai atau fix
         */

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    findUser(query)
                }
                return true
            }

            /*
            method ini untuk merespon setiap perubahan query
             */
            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }

        })
        return true


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id =  item.itemId

        if (id == R.id.settings){
            startActivity(Intent(this,SettingActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun moveShowSelectUser(user: ItemsItem){
        startActivity(Intent(this@MainActivity, DetailUserActivity::class.java).putExtra("username",user.login))
    }

    private fun findUser(query: String){
        //start loading
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(window.decorView.rootView.windowToken,0)
        loading(true)


        val client =  ApiConfig.getApiService().getUser(this.getString(R.string.github_key_api),query)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: retrofit2.Response<SearchResponse>
            ) {
                val responseBody = response.body()
                loading(false)
                if (response.isSuccessful&&responseBody!= null){
                    if(responseBody.totalCount >0) {

                        rv_user.visibility = View.VISIBLE

                        setReviewData(responseBody.items)
                    }else{
                        Toast.makeText(this@MainActivity,"Tidak Menemukan Data", Toast.LENGTH_SHORT).show()
                    }

                }else{

                    Toast.makeText(this@MainActivity,"Gagal Memuat Data", Toast.LENGTH_SHORT).show()
                    Log.e(TAG,"onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                loading(false)
                Toast.makeText(this@MainActivity,"Gagal Memuat Data", Toast.LENGTH_SHORT).show()
                Log.e(TAG,"onFailure: ${t.message}")
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

    private fun setReviewData(items: List<ItemsItem>) {


        rv_user.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(items)
        rv_user.adapter =listUserAdapter
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                moveShowSelectUser(data)
            }

        })
    }

}