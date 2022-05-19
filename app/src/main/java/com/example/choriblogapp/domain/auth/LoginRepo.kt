package com.example.choriblogapp.domain.auth

import com.google.firebase.auth.FirebaseUser

interface LoginRepo {

    suspend fun signIn(email:String,password:String):FirebaseUser?
}