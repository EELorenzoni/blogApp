package com.example.choriblogapp.ui.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.choriblogapp.R
import com.example.choriblogapp.core.Result
import com.example.choriblogapp.data.remote.auth.AuthDataSource
import com.example.choriblogapp.databinding.FragmentSetupProfileBinding
import com.example.choriblogapp.domain.auth.AuthRepoImpl
import com.example.choriblogapp.presentation.auth.AuthViewModel
import com.example.choriblogapp.presentation.auth.AuthViewModelFactory

class SetupProfileFragment : Fragment(R.layout.fragment_setup_profile) {
    private lateinit var binding: FragmentSetupProfileBinding

    private var REQUEST_IMAGE_CAPTURE: Int = -1
    private var bitmap: Bitmap? = null
    private val responsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == REQUEST_IMAGE_CAPTURE && it.resultCode == Activity.RESULT_OK) {
                val imageBitmap = it.data?.extras?.get("data") as Bitmap
                binding.profileImage.setImageBitmap(imageBitmap)
                bitmap = imageBitmap
            }
        }

    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepoImpl(
                AuthDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupProfileBinding.bind(view)
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        binding.profileImage.setOnClickListener {
            try {
                //lanzar camara
                responsLauncher.launch(takePicture)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    requireContext(),
                    "No se encontró ninguna app para abrir la cámara.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnCreaeProfile.setOnClickListener {
            val username = binding.txtUsername.text.toString().trim()
            val alertDialog =
                AlertDialog.Builder(requireContext()).setTitle("Uploading photo...").create()
            bitmap?.let {
                if (username.isNotEmpty()) {
                    viewModel.updateUserProfile(imageBitmap = it, username = username)
                        .observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is Result.Loading -> {
                                    alertDialog.show()
                                }
                                is Result.Success -> {
                                    alertDialog.dismiss()
                                    findNavController().navigate(R.id.action_setupProfileFragment_to_homeScreenFragment)
                                }
                                is Result.Failure -> {
                                    alertDialog.dismiss()
                                }
                            }
                        }
                }
            }
        }
    }
}