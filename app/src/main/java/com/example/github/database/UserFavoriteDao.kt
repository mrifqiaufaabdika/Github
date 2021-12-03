package com.example.github.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserFavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun Insert(UserFavorite: UserFavorite)

    @Update
    fun update(UserFavorite: UserFavorite)

    @Delete
    fun delete(UserFavorite: UserFavorite)

    @Query("Select * from userfavorite ORDER BY id_user ASC")
    fun getAllUserFavorite(): LiveData<List<UserFavorite>>

    @Query("Select COUNT(*) from userfavorite WHERE login = :login")
    fun getOne(login : String) : Int
}