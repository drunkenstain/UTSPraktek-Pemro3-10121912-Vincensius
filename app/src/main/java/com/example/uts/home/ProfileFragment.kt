package com.example.uts.home

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.uts.R
import java.io.File

class ProfileFragment : Fragment() {

    private val PICK_IMAGE_REQUEST = 1
    private val STORAGE_PERMISSION_CODE = 101

    private lateinit var profileImageView: ImageView
    private lateinit var profileNameTextView: TextView
    private lateinit var profileDescriptionTextView: TextView
    private lateinit var editProfileButton: Button

    private val PREFS_NAME = "profile_prefs"
    private val PROFILE_IMAGE_URI_KEY = "profile_image_uri"
    private val PROFILE_NAME_KEY = "profile_name"
    private val PROFILE_DESCRIPTION_KEY = "profile_description"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileImageView = view.findViewById(R.id.profile_image)
        profileNameTextView = view.findViewById(R.id.profile_name)
        profileDescriptionTextView = view.findViewById(R.id.profile_description)
        editProfileButton = view.findViewById(R.id.btn_edit_profile)

        loadProfileData()

        profileImageView.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // No need to request permission for storage access on Android 10 and above
                openImagePicker()
            } else {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
                } else {
                    openImagePicker()
                }
            }
        }

        editProfileButton.setOnClickListener {
            showEditProfileDialog()
        }

        return view
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Permission Denied")
                    .setMessage("Permission to access external storage is required to select a profile image.")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri? = data.data
            profileImageView.setImageURI(selectedImageUri)
            saveProfileImage(selectedImageUri)
        }
    }

    private fun showEditProfileDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_edit_profile, null)
        val editName = dialogLayout.findViewById<EditText>(R.id.edit_name)
        val editDescription = dialogLayout.findViewById<EditText>(R.id.edit_description)

        editName.setText(profileNameTextView.text.toString())
        editDescription.setText(profileDescriptionTextView.text.toString())

        builder.setView(dialogLayout)
        builder.setPositiveButton("Save") { dialog, which ->
            val newName = editName.text.toString()
            val newDescription = editDescription.text.toString()
            profileNameTextView.text = newName
            profileDescriptionTextView.text = newDescription
            saveProfileData(newName, newDescription)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun saveProfileImage(imageUri: Uri?) {
        if (imageUri != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = requireActivity().contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "profile_image.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "Profile")
                }
                val newImageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                resolver.openOutputStream(newImageUri!!).use { outputStream ->
                    val bitmap = (profileImageView.drawable as BitmapDrawable).bitmap
                    if (outputStream != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }
                }
                // Simpan URI gambar baru ke SharedPreferences
                val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString(PROFILE_IMAGE_URI_KEY, newImageUri.toString())
                editor.apply()
            } else {
                val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString(PROFILE_IMAGE_URI_KEY, imageUri.toString())
                editor.apply()
            }
        }
    }

    private fun saveProfileData(name: String, description: String) {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(PROFILE_NAME_KEY, name)
        editor.putString(PROFILE_DESCRIPTION_KEY, description)
        editor.apply()
    }

    private fun loadProfileData() {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val imageUriString = sharedPreferences.getString(PROFILE_IMAGE_URI_KEY, null)
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            profileImageView.setImageURI(imageUri)
        }

        val name = sharedPreferences.getString(PROFILE_NAME_KEY, "Your Name")
        profileNameTextView.text = name

        val description = sharedPreferences.getString(PROFILE_DESCRIPTION_KEY, "Short bio or description about you.")
        profileDescriptionTextView.text = description
    }
}
