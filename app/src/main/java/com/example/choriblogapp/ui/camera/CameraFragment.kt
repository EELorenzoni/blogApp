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
import com.example.choriblogapp.R
import com.example.choriblogapp.databinding.FragmentCameraBinding


class CameraFragment : Fragment(R.layout.fragment_camera) {
    private lateinit var binding: FragmentCameraBinding

    private var REQUEST_IMAGE_CAPTURE: Int = -1

    private val responsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == REQUEST_IMAGE_CAPTURE && it.resultCode == RESULT_OK) {
                val imageBitmap = it.data?.extras?.get("data") as Bitmap
                binding.imgAddPhoto.setImageBitmap(imageBitmap)
            }
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
    }


}