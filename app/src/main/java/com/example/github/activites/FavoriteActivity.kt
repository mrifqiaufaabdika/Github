package com.example.github.activites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.github.adapters.UserFavotitAdapter
import com.example.github.database.UserFavorite
import com.example.github.databinding.ActivityUserFavoriteBinding
import com.example.github.datastore.SettingPreferences
import com.example.github.ui.insert.UserFavoriteAddViewModel
import com.example.github.ui.main.MainViewModel
import com.example.github.ui.main.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserFavoriteBinding
    private lateinit var rvUser: RecyclerView

    private lateinit var userFavoriteAddViewModel: UserFavoriteAddViewModel

    private lateinit var adapter : UserFavotitAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvUser = binding.rvFavoriteUser

        userFavoriteAddViewModel = obtainUserFavoriteViewModel(this)

        setTitle("User Favorite")

        setData()


    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        var pref = SettingPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory).get(MainViewModel::class.java)
    }

    private fun obtainUserFavoriteViewModel(activity: AppCompatActivity): UserFavoriteAddViewModel {
        var pref = SettingPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(activity.application, pref)
        return ViewModelProvider(activity, factory).get(UserFavoriteAddViewModel::class.java)
    }

    private fun setData() {
        rvUser.layoutManager = LinearLayoutManager(this)
        rvUser.setHasFixedSize(true)


        val mainViewModel = obtainViewModel(this)
        mainViewModel.getAllUserFavorites().observe(this, { userFavoriteList ->
            if (userFavoriteList.isNotEmpty()) {

                adapter.setListUservaforite(userFavoriteList)
                adapter.setOnItemClickCallback(object : UserFavotitAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: UserFavorite) {
                        startActivity(
                            Intent(
                                this@FavoriteActivity,
                                DetailUserActivity::class.java
                            ).putExtra("username", data.login)
                        )
                    }

                    override fun onItemDelete(data: UserFavorite) {

                        userFavoriteAddViewModel.delete(data)
                    }
                })
            } else {
                Toast.makeText(this, "Tidak Ada Data Favorite", Toast.LENGTH_SHORT).show()
            }
        })

        adapter = UserFavotitAdapter()
        rvUser.adapter = adapter


    }


}