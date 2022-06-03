package com.example.choriblogapp.data.remote.home

import com.example.choriblogapp.core.Result
import com.example.choriblogapp.data.model.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception

class HomeScreenDataSource {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getLatestPosts(): Flow<Result<List<Post>>> = callbackFlow {
        val postList = mutableListOf<Post>()
        var postReference: Query? = null

        try {
            postReference = FirebaseFirestore.getInstance().collection("posts")
                .orderBy("created_at", Query.Direction.DESCENDING)
        } catch (e: Throwable) {
            close(e)
        }
        val suscription = postReference?.addSnapshotListener { value, error ->
            if (value == null) return@addSnapshotListener
            try {
                postList.clear()
                for (post in value.documents) {

                    post.toObject(Post::class.java)?.let {
                        it.apply {
                            created_at = post.getTimestamp(
                                "created_at",
                                DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                            )?.toDate()
                        }
                        postList.add(it)
                    }
                }

            } catch (e: Exception) {
                close(e)
            }
            trySend(Result.Success(postList)).isSuccess
        }
        awaitClose { suscription?.remove() }
    }
}