package com.example.choriblogapp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.lang.RuntimeException
import java.util.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}


/*
private lateinit var imageView: ImageView
private lateinit var resultLauncher: ActivityResultLauncher<Intent?>

*/

//val btnTakePicture = findViewById<Button>(R.id.btn_take_picture)
//imageView = findViewById(R.id.imageView)
//
//resultLauncher = registerForActivityResult(
//ActivityResultContracts
//.StartActivityForResult()
//) {
//    if (it.resultCode == Activity.RESULT_OK) {
//        val data: Intent? = it.data
//        val imageBitmap = data?.extras?.get("data") as Bitmap
//        imageView.setImageBitmap(imageBitmap)
//        uploadPicture(imageBitmap)
//    }
//}
//
//btnTakePicture.setOnClickListener {
//    dispatchTakePictureIntent()
//}
//
//imageView = findViewById(R.id.imageView)
//
//btnTakePicture.setOnClickListener {
//    dispatchTakePictureIntent()
//}
//
//throw RuntimeException("Crashlytics test")


/*

private fun dispatchTakePictureIntent() {
    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    try {
        resultLauncher.launch(takePictureIntent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "No se encontró app para tomar la foto", Toast.LENGTH_SHORT).show()
    }
}

private fun uploadPicture(bitmap: Bitmap) {
    val storageRef = FirebaseStorage.getInstance().reference
    val imagesRef = storageRef.child("imagenes/${UUID.randomUUID()}.jpg")
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val data = baos.toByteArray()
    val upladTask = imagesRef.putBytes(data)

    upladTask.continueWithTask { task ->
        if (!task.isSuccessful) {
            task.exception?.let {
                Log.d("error en subida", it.toString())
                throw it
            }
        }
        imagesRef.downloadUrl
    }.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val downloadUrl = task.result.toString()
            FirebaseFirestore.getInstance().collection("ciudades").document("LA")
                .update(mapOf("imageUrl" to downloadUrl))
            Log.d("Storage", "uploadPictureURL: $downloadUrl")
        }
    }
}*/
