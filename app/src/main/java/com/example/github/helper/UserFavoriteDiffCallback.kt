package com.example.github.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.github.database.UserFavorite

class UserFavoriteDiffCallback( private val mOldUserFavorite : List<UserFavorite>, private val mNewUserFavoriteList:List<UserFavorite>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldUserFavorite.size
    }

    override fun getNewListSize(): Int {
        return mNewUserFavoriteList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUserFavorite[oldItemPosition].id_user == mNewUserFavoriteList[newItemPosition].id_user
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        var old = mOldUserFavorite[oldItemPosition]
        var new = mOldUserFavorite[newItemPosition]
        return old.login == new.login
    }
}