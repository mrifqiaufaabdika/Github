package com.example.github.ui.insert

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.github.database.UserFavorite
import com.example.github.repository.UserFavoriteRepository

class UserFavoriteAddViewModel(application: Application) : ViewModel() {

    private val mUserFavoriteRepository: UserFavoriteRepository =
        UserFavoriteRepository(application)

    fun insert(userFavorite: UserFavorite) {
        mUserFavoriteRepository.insert(userFavorite)
    }

    fun update(userFavorite: UserFavorite) {
        mUserFavoriteRepository.update(userFavorite)
    }

    fun delete(userFavorite: UserFavorite) {
        mUserFavoriteRepository.delete(userFavorite)
    }

    fun getOne(login: String)  : Int = mUserFavoriteRepository.getOne(login)
}