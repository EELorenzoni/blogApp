package com.example.choriblogapp.ui.main

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.choriblogapp.R
import com.example.choriblogapp.core.Result
import com.example.choriblogapp.core.hide
import com.example.choriblogapp.core.show
import com.example.choriblogapp.data.remote.home.HomeScreenDataSource
import com.example.choriblogapp.databinding.FragmentHomeScreenBinding
import com.example.choriblogapp.domain.home.HomeScreenRepoImpl
import com.example.choriblogapp.presentation.HomeScreenViewModel
import com.example.choriblogapp.presentation.HomeScreenViewModelFactory
import com.example.choriblogapp.ui.main.adapters.HomeScreenAdapter

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {
    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel by viewModels<HomeScreenViewModel> {
        HomeScreenViewModelFactory(
            HomeScreenRepoImpl(
                HomeScreenDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeScreenBinding.bind(view)
        viewModel.fetchLatestPosts().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.show()
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    if (result.data.isEmpty()) {
                        binding.emptyContainer.show()
                        return@observe
                    } else {
                        binding.emptyContainer.hide()
                    }
                    binding.rvHome.adapter = HomeScreenAdapter(result.data)
                }
                is Result.Failure -> {
                    binding.progressBar.hide()
                    Toast.makeText(
                        requireContext(),
                        "Ocurrio un error: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
//        binding.rvHome.adapter = HomeScreenAdapter(postList)
    }
}