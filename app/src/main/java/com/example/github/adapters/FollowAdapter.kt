package com.example.github.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.github.databinding.SingleRowBinding
import com.example.github.responses.FollowResponse


class FollowAdapter(private val listUser: List<FollowResponse>) :
    RecyclerView.Adapter<FollowAdapter.ListViewHolder>() {


    class ListViewHolder(val binding: SingleRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = SingleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.delete.visibility = View.GONE
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


            }
        }
    }


    override fun getItemCount(): Int = listUser.size


}