package com.example.choriblogapp.domain.home

import com.example.choriblogapp.core.Resource
import com.example.choriblogapp.data.model.Post

interface HomeScreenRepo {
    suspend fun getLatestPosts():Resource<List<Post>>

}