package com.example.choriblogapp.ui.home.adapters

import android.content.Context
import android.text.BoringLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.choriblogapp.R
import com.example.choriblogapp.core.BaseViewHolder
import com.example.choriblogapp.core.TimeUtils
import com.example.choriblogapp.data.model.Post
import com.example.choriblogapp.databinding.PostItemViewBinding

class HomeScreenAdapter(
    private val postList: List<Post>,
    private val onPostClickListener: OnPostClickListener
) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var postClickListener: OnPostClickListener? = null

    init {
        postClickListener = onPostClickListener
    }

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
            setupPrfolieInfo(item)
            addPosTimeStamp(item)
            setupPostImage(item)
            setupPostDescription(item)
            tintHeartIcon(item)
            setupLikeCoun(item)
            setLikeClickAcion(item)
        }


        private fun setupPrfolieInfo(post: Post) {
            Glide.with(context).load(post.poster?.profile_picture).centerCrop()
                .into(binding.profilePicture)
            binding.profileName.text = post.poster?.username
        }

        private fun addPosTimeStamp(post: Post) {
            post.created_at?.let {
                binding.postTimestamp.text = TimeUtils.getTimeAgo(it)
            }
        }

        private fun setupPostImage(post: Post) {
            Glide.with(context).load(post.post_image).centerCrop().into(binding.postImage)

        }

        private fun setupPostDescription(post: Post) {
            if (post.post_description.isEmpty()) {
                binding.postDescription.visibility = View.GONE
            } else {
                binding.postDescription.text = post.post_description
            }
        }

        private fun tintHeartIcon(post: Post) {
            if (!post.liked) {
                binding.likeBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_baseline_favorite_border_24
                    )
                )
                binding.likeBtn.setColorFilter(ContextCompat.getColor(context, R.color.black))
            } else {
                binding.likeBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_baseline_favorite_24
                    )
                )
                binding.likeBtn.setColorFilter(ContextCompat.getColor(context, R.color.red_like))

            }
        }

        private fun setupLikeCoun(post: Post) {
            if (post.likes > 0) {
                binding.likeCount.visibility = View.VISIBLE
                binding.likeCount.text = "${post.likes} likes"
            } else {
                binding.likeCount.visibility = View.GONE

            }
        }

        private fun setLikeClickAcion(post: Post) {
            binding.likeBtn.setOnClickListener {
                if (post.liked) post.apply { liked = false } else post.apply { liked = true }
                tintHeartIcon(post)
                postClickListener?.onLikeButtonClick(post, post.liked)
            }
        }

    }
}

interface OnPostClickListener {
    fun onLikeButtonClick(post: Post, liked: Boolean)
}