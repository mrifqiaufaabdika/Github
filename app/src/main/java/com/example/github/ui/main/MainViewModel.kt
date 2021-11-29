package com.example.github.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.github.database.UserFavorite
import com.example.github.datastore.SettingPreferences
import com.example.github.repository.UserFavoriteRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application, private val pref: SettingPreferences) : ViewModel() {
    private val mUserFavoriteRepository: UserFavoriteRepository =
        UserFavoriteRepository(application)


    fun getAllUserFavorites(): LiveData<List<UserFavorite>> =
        mUserFavoriteRepository.getAllUserFavorite()

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}