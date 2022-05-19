package com.example.choriblogapp.domain.auth

import com.example.choriblogapp.data.remote.auth.LoginDataSource
import com.google.firebase.auth.FirebaseUser

class LoginRepoImpl (private val dataSource: LoginDataSource): LoginRepo {
    override suspend fun signIn(email: String, password: String): FirebaseUser? {
        return dataSource.signIn(email,password)
    }
}