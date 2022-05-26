package com.example.choriblogapp.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.choriblogapp.R
import com.example.choriblogapp.core.Result
import com.example.choriblogapp.data.remote.auth.AuthDataSource
import com.example.choriblogapp.databinding.FragmentRegisterBinding
import com.example.choriblogapp.domain.auth.AuthRepoImpl
import com.example.choriblogapp.presentation.auth.AuthViewModel
import com.example.choriblogapp.presentation.auth.AuthViewModelFactory

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding

    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(AuthRepoImpl(AuthDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        getSignUpInfo()
    }

    private fun getSignUpInfo() {


        binding.btnSingup.setOnClickListener {
            val username = binding.textInputLayoutUsername.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()
            val email = binding.textInputLayoutEmail.text.toString().trim()

            if (validateUserData(
                    confirmPassword,
                    password,
                    username,
                    email
                )
            ) return@setOnClickListener

            createUser(email, password, username)
        }
    }

    private fun createUser(email: String, password: String, username: String) {
        viewModel.signUp(email, password, username).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnSingup.isEnabled = false
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_registerFragment_to_setupProfileFragment)
                }
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnSingup.isEnabled = true
                    Toast.makeText(
                        requireContext(),
                        "Error: ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun validateUserData(
        confirmPassword: String,
        password: String,
        username: String,
        email: String
    ): Boolean {
        if (confirmPassword != password) {
            binding.editTextPassword.error = "Password does not match"
            binding.editTextConfirmPassword.error = "Password does not match"
            return true
        }

        if (username.isEmpty()) {
            binding.textInputLayoutUsername.error = "Username is empty"
            return true
        }
        if (email.isEmpty()) {
            binding.textInputLayoutEmail.error = "Email is empty"
            return true
        }
        if (password.isEmpty()) {
            binding.editTextPassword.error = "Password is empty"
            return true
        }
        if (confirmPassword.isEmpty()) {
            binding.editTextConfirmPassword.error = "Confirm password is empty"
            return true
        }
        return false
    }
}