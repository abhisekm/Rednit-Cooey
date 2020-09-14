package com.abhisek.rednit.ui.profileList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abhisek.rednit.R
import com.abhisek.rednit.databinding.ProfileItemLayoutBinding
import com.abhisek.rednit.domain.Profile

class ProfileListAdapter : ListAdapter<Profile, ProfileListAdapter.ProfileViewHolder>(DiffCallback){

    companion object DiffCallback : DiffUtil.ItemCallback<Profile>() {
        override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            return oldItem == newItem
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        return ProfileViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                ProfileViewHolder.LAYOUT,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProfileViewHolder(private val binding: ProfileItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.profile_item_layout
        }

        fun bind(profile: Profile) {
            binding.profile = profile
            binding.executePendingBindings()
        }
    }


}