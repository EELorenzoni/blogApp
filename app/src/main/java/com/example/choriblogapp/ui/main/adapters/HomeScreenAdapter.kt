package com.example.choriblogapp.ui.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.choriblogapp.core.BaseViewHolder
import com.example.choriblogapp.core.TimeUtils
import com.example.choriblogapp.data.model.Post
import com.example.choriblogapp.databinding.PostItemViewBinding

class HomeScreenAdapter(private val postList: List<Post>) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemViewBinding =
            PostItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HomeScreenViewHolder(itemViewBinding, parent.context)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is HomeScreenViewHolder -> holder.bind(postList[position])
        }
    }

    override fun getItemCount(): Int = postList.size

    private inner class HomeScreenViewHolder(
        val binding: PostItemViewBinding,
        val context: Context
    ) : BaseViewHolder<Post>(binding.root) {
        override fun bind(item: Post) {
            Glide.with(context).load(item.post_image).centerCrop().into(binding.postImage)
            Glide.with(context).load(item.post_image).centerCrop().into(binding.profilePicture)
            binding.profileName.text = item.profile_name
            if (item.post_description.isEmpty()) {
                binding.postDescription.visibility = View.GONE
            } else {
                binding.postDescription.text = item.post_description
            }
            item.created_at?.let {
                binding.postTimestamp.text = TimeUtils.getTimeAgo(it)
            }
        }
    }
}