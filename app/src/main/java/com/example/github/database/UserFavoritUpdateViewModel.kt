package com.example.github.database

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.github.repository.UserFavoriteRepository

class UserFavoritUpdateViewModel(application: Application) : ViewModel() {

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
}