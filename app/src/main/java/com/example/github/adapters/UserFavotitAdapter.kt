package com.example.github.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.github.database.UserFavorite
import com.example.github.databinding.SingleRowBinding
import com.example.github.helper.UserFavoriteDiffCallback

class UserFavotitAdapter() : RecyclerView.Adapter<UserFavotitAdapter.ListViewHolder>() {

    private val listUser = ArrayList<UserFavorite>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(val binding: SingleRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = SingleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val Item = listUser[position]
        with(holder) {
            with(listUser[position]) {
                binding.name.text = Item.login
                binding.username.text = Item.login
                Glide.with(holder.itemView.context)
                    .load(Item.avatarUrl)
                    .circleCrop()
                    .into(binding.avatar)

                holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
                binding.delete.setOnClickListener { onItemClickCallback.onItemDelete(listUser[holder.adapterPosition]) }

            }
        }
    }


    override fun getItemCount(): Int = listUser.size

    fun setListUservaforite(listUser: List<UserFavorite>) {
        val diffCallback = UserFavoriteDiffCallback(this.listUser, listUser)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listUser.clear()
        this.listUser.addAll(listUser)
        diffResult.dispatchUpdatesTo(this)
    }


    interface OnItemClickCallback {
        fun onItemClicked(data: UserFavorite)
        fun onItemDelete(data: UserFavorite)
    }

}