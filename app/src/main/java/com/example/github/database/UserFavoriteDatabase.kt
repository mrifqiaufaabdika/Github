package com.example.github.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserFavorite::class], version = 1)
abstract class UserFavoriteDatabase : RoomDatabase() {

    abstract fun userFavoriteDao(): UserFavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: UserFavoriteDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserFavoriteDatabase {
            if (INSTANCE == null) {
                synchronized(UserFavoriteDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserFavoriteDatabase::class.java,
                        "UserFavorite_Database"
                    ).build()
                }
            }
            return INSTANCE as UserFavoriteDatabase
        }
    }
}