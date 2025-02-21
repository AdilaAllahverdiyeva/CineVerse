package com.example.cineverse

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class MeFragment : Fragment(R.layout.fragment_me) {

    private lateinit var profileImageView: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var logOutBtn: Button

    private val CAMERA_PERMISSION_CODE = 101
    private val CAMERA_REQUEST_CODE = 102

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImageView = view.findViewById(R.id.ivProfileImage)
        selectImageButton = view.findViewById(R.id.btnChangePhoto)

        logOutBtn = view.findViewById(R.id.logOutBtn)

        logOutBtn.setOnClickListener {
            findNavController().navigate(R.id.action_meFragment_to_loginFragment)
        }
        selectImageButton.setOnClickListener {
            showImagePickerDialog()
        }
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("📷 Taking pictures with a camera", "🖼️ Choose from gallery")
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Change photo")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> checkCameraPermission() // Kamera icazəsini yoxlayır və açır
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "📸 Kamera icazəsi lazımdır!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(requireContext(), "Kamera açılmadı!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(intent)
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            profileImageView.setImageURI(imageUri)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            profileImageView.setImageBitmap(imageBitmap)
        }
    }
}
