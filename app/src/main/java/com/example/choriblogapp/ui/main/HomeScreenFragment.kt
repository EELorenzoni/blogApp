package com.example.choriblogapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.choriblogapp.R
import com.example.choriblogapp.core.Resource
import com.example.choriblogapp.data.remote.home.HomeScreenDataSource
import com.example.choriblogapp.databinding.FragmentHomeScreenBinding
import com.example.choriblogapp.domain.home.HomeScreenRepoImpl
import com.example.choriblogapp.presentation.HomeScreenViewModel
import com.example.choriblogapp.presentation.HomeScreenViewModelFactory
import com.example.choriblogapp.ui.main.adapters.HomeScreenAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility=View.GONE
                    binding.rvHome.adapter = HomeScreenAdapter(result.data)
                }
                is Resource.Failure -> {
                    binding.progressBar.visibility=View.GONE
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