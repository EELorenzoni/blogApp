package com.example.choriblogapp.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.choriblogapp.R
import com.example.choriblogapp.core.Resource
import com.example.choriblogapp.data.remote.auth.LoginDataSource
import com.example.choriblogapp.databinding.FragmentLoginBinding
import com.example.choriblogapp.domain.auth.LoginRepoImpl
import com.example.choriblogapp.presentation.auth.LoginScreenViewModel
import com.example.choriblogapp.presentation.auth.LoginScreenViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val viewModel by viewModels<LoginScreenViewModel> {
        LoginScreenViewModelFactory(LoginRepoImpl(LoginDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        isUserLoggedIn()
        doLogin()
    }

    private fun isUserLoggedIn() {
        firebaseAuth.currentUser?.let { findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment) }
    }

    private fun doLogin() {
        binding.btnSignin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextassword.text.toString().trim()
            validateCredentials(email, password)
            signIn(email, password)
        }

    }

    private fun validateCredentials(email: String, password: String) {
        if (email.isEmpty()) {
            binding.editTextEmail.error = "E-mail is empty"
            return
        }

        if (password.isEmpty()) {
            binding.editTextassword.error = "Password is empty"
            return
        }
    }

    private fun signIn(email: String, password: String) {
        viewModel.signIn(email, password).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSignin.isEnabled = false
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment)
                }
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSignin.isEnabled = true
                    Toast.makeText(requireContext(),"Error-Login: ${result.exception}",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}