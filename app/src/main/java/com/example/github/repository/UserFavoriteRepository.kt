package com.example.github.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.github.database.UserFavorite
import com.example.github.database.UserFavoriteDao
import com.example.github.database.UserFavoriteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserFavoriteRepository(application: Application) {

    private val mUserFavoriteDao: UserFavoriteDao

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserFavoriteDatabase.getDatabase(application)
        mUserFavoriteDao = db.userFavoriteDao()
    }

    fun getAllUserFavorite(): LiveData<List<UserFavorite>> = mUserFavoriteDao.getAllUserFavorite()

    fun insert(userFavorite: UserFavorite) {
        executorService.execute {
            mUserFavoriteDao.Insert(userFavorite)
        }
    }

    fun delete(userFavorite: UserFavorite) {
        executorService.execute {
            mUserFavoriteDao.delete(userFavorite)
        }
    }

    fun update(userFavorite: UserFavorite) {
        executorService.execute {
            mUserFavoriteDao.update(userFavorite)
        }
    }

    fun getOne(login : String) : Int = mUserFavoriteDao.getOne(login)




}