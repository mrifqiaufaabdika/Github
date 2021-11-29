package com.example.github.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class UserFavorite(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_user")
    var id_user: Int = 0,


    @ColumnInfo(name = "login")
    var login: String? = null,

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null,

    ) : Parcelable
