package com.example.choriblogapp.domain.home

import com.example.choriblogapp.core.Result
import com.example.choriblogapp.data.model.Post

interface HomeScreenRepo {
    suspend fun getLatestPosts(): Result<List<Post>>

}