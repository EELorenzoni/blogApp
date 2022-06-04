package com.example.choriblogapp.data.remote.home

import com.example.choriblogapp.core.Result
import com.example.choriblogapp.data.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class HomeScreenDataSource {
    suspend fun getLatestPosts(): Result<List<Post>> {
        val postList = mutableListOf<Post>()

        withContext(Dispatchers.IO) {
            val queryShanpsHot = FirebaseFirestore.getInstance().collection("posts")
                .orderBy("created_at", Query.Direction.DESCENDING).get().await()
            for (post in queryShanpsHot.documents) {

                post.toObject(Post::class.java)?.let {
                    val isLiked = FirebaseAuth.getInstance().currentUser?.let { safeUser ->
                        isPostLiked(
                            post.id,
                            safeUser.uid
                        )
                    }
                    it.apply {
                        created_at = post.getTimestamp(
                            "created_at",
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )?.toDate()
                        id = post.id

                        if (isLiked != null) liked = isLiked
                    }
                    postList.add(it)
                }
            }

        }
        return Result.Success(postList)
    }

    private suspend fun isPostLiked(postId: String, uid: String): Boolean {
        val post =
            FirebaseFirestore.getInstance().collection("postLikes").document(postId).get().await()
        if (!post.exists()) return false
        val likeArray: List<String> = post.get("likes") as List<String>
        return likeArray.contains(uid)
    }

    fun registerLikeButtonState(postId: String, liked: Boolean) {
        val increment = FieldValue.increment(1)
        val decrement = FieldValue.increment(-1)
        val database = FirebaseFirestore.getInstance()

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val postRef = database.collection("posts").document(postId)
        val postsLikesRef = database.collection("postsLikes").document(postId)

        database.runTransaction { transacion ->
            val snapshot = transacion.get(postRef)
            val likeCount = snapshot.getLong("likes")
            likeCount?.let {
                if (likeCount >= 0) {
                    if (liked) {
                        if (transacion.get(postsLikesRef).exists()) {
                            transacion.update(postsLikesRef, "likes", FieldValue.arrayUnion(uid))
                        } else {
                            transacion.set(
                                postsLikesRef, hashMapOf("likes" to arrayListOf(uid)),
                                SetOptions.merge()
                            )
                        }
                        transacion.update(postRef, "likes", increment)
                    } else {
                        transacion.update(postRef, "likes", decrement)
                        transacion.update(postsLikesRef, "likes", FieldValue.arrayRemove(uid))
                    }
                }
            }

        }.addOnFailureListener {
            throw Exception(it.message)
        }
    }
}


//    @OptIn(ExperimentalCoroutinesApi::class)
//    suspend fun getLatestPosts(): Flow<Result<List<Post>>> = callbackFlow {
//        val postList = mutableListOf<Post>()
//        var postReference: Query? = null
//
//        try {
//            postReference = FirebaseFirestore.getInstance().collection("posts")
//                .orderBy("created_at", Query.Direction.DESCENDING)
//        } catch (e: Throwable) {
//            close(e)
//        }
//        val suscription = postReference?.addSnapshotListener { value, error ->
//            if (value == null) return@addSnapshotListener
//            try {
//                postList.clear()
//                for (post in value.documents) {
//
//                    post.toObject(Post::class.java)?.let {
//                        it.apply {
//                            created_at = post.getTimestamp(
//                                "created_at",
//                                DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
//                            )?.toDate()
//                        }
//                        postList.add(it)
//                    }
//                }
//
//            } catch (e: Exception) {
//                close(e)
//            }
//            trySend(Result.Success(postList)).isSuccess
//        }
//        awaitClose { suscription?.remove() }
//    }
