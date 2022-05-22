package com.example.choriblogapp.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.choriblogapp.R
import com.example.choriblogapp.core.Result
import com.example.choriblogapp.data.remote.auth.AuthDataSource
import com.example.choriblogapp.databinding.FragmentLoginBinding
import com.example.choriblogapp.domain.auth.AuthRepoImpl
import com.example.choriblogapp.presentation.auth.AuthViewModel
import com.example.choriblogapp.presentation.auth.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(AuthRepoImpl(AuthDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        isUserLoggedIn()
        doLogin()
        goToSignUpPage()
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

    private fun goToSignUpPage() {
        binding.txtSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSignin.isEnabled = false
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment)
                }
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSignin.isEnabled = true
                    Toast.makeText(
                        requireContext(),
                        "Error-Login: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }
}