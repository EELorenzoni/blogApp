package com.example.choriblogapp.domain.home

import com.example.choriblogapp.core.Result
import com.example.choriblogapp.data.model.Post
import com.example.choriblogapp.data.remote.home.HomeScreenDataSource

class HomeScreenRepoImpl (private val dataSource: HomeScreenDataSource): HomeScreenRepo {
//    override suspend fun getLatestPosts(): Flow<Result<List<Post>>> = dataSource.getLatestPosts()
    override suspend fun getLatestPosts(): Result<List<Post>> = dataSource.getLatestPosts()
    override suspend fun registerLikeButtonState(postId: String, liked: Boolean)= dataSource.registerLikeButtonState(postId,liked)
}