package com.example.choriblogapp.ui.camera

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.choriblogapp.R
import com.example.choriblogapp.core.Result
import com.example.choriblogapp.data.remote.camera.CameraDataSource
import com.example.choriblogapp.data.remote.home.HomeScreenDataSource
import com.example.choriblogapp.databinding.FragmentCameraBinding
import com.example.choriblogapp.domain.camera.CameraRepoImpl
import com.example.choriblogapp.domain.home.HomeScreenRepoImpl
import com.example.choriblogapp.presentation.HomeScreenViewModel
import com.example.choriblogapp.presentation.HomeScreenViewModelFactory
import com.example.choriblogapp.presentation.camera.CameraViewModel
import com.example.choriblogapp.presentation.camera.CameraViewModelFactory


class CameraFragment : Fragment(R.layout.fragment_camera) {
    private lateinit var binding: FragmentCameraBinding

    private var REQUEST_IMAGE_CAPTURE: Int = -1
    private var bitmap: Bitmap? = null
    private val responsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == REQUEST_IMAGE_CAPTURE && it.resultCode == RESULT_OK) {
                val imageBitmap = it.data?.extras?.get("data") as Bitmap
                binding.postImage.setImageBitmap(imageBitmap)
                bitmap = imageBitmap
            }
        }

    private val viewModel by viewModels<CameraViewModel> {
        CameraViewModelFactory(
            CameraRepoImpl(
                CameraDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        binding = FragmentCameraBinding.bind(view)
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

        binding.btnUploadPhoto.setOnClickListener {
            bitmap?.let {
                viewModel.uploadPhoto(it, binding.txtDescription.text.toString().trim())
                    .observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Result.Loading -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Uploading photo...",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is Result.Success -> {
                                findNavController().navigate(R.id.action_cameraFragment_to_homeScreenFragment)
                            }
                            is Result.Failure -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Error ${result.exception}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            }
        }
    }


}