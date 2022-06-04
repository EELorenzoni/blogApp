package com.example.choriblogapp.domain.home

import com.example.choriblogapp.core.Result
import com.example.choriblogapp.data.model.Post
import kotlinx.coroutines.flow.Flow

interface HomeScreenRepo {
    //    suspend fun getLatestPosts(): Flow<Result<List<Post>>>
    suspend fun getLatestPosts(): Result<List<Post>>

    suspend fun registerLikeButtonState(postId: String, liked: Boolean)
}