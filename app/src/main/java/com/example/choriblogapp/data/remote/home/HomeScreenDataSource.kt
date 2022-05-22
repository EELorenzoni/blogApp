package com.example.choriblogapp.data.remote.home

import com.example.choriblogapp.core.Result
import com.example.choriblogapp.data.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class HomeScreenDataSource {
    suspend fun getLatestPosts(): Result<List<Post>> {
        val postList = mutableListOf<Post>()
        val querySnapshot = FirebaseFirestore.getInstance().collection("posts").get().await()
        for (post in querySnapshot.documents) {
            post.toObject(Post::class.java)?.let { postList.add(it) }
        }
        return Result.Success(postList)
    }
}