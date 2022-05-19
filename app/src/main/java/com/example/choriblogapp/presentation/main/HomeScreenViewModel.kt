package com.example.choriblogapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.choriblogapp.core.Resource
import com.example.choriblogapp.core.Resource.Failure
import com.example.choriblogapp.domain.home.HomeScreenRepo
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class HomeScreenViewModel(private val repo: HomeScreenRepo) : ViewModel() {

    fun fetchLatestPosts() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(repo.getLatestPosts())
        } catch (e: Exception) {
            emit(Failure(e))
        }
    }
}

class HomeScreenViewModelFactory(private val repo: HomeScreenRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HomeScreenRepo::class.java).newInstance(repo)
    }
}