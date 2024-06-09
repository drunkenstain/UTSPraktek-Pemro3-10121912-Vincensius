package com.example.uts.other

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uts.R
import com.example.uts.gallery.GalleryAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONObject

class GalleryFragment : Fragment(), GalleryAdapter.GalleryItemClickListener {

    data class GalleryItem(var imageUri: String, var name: String)

    private lateinit var adapter: GalleryAdapter
    private val galleryItems = mutableListOf<GalleryItem>()
    private var selectedPosition: Int? = null
    private val PICK_IMAGE_REQUEST = 1
    private val PREFS_NAME = "gallery_prefs"
    private val KEY_GALLERY_ITEMS = "gallery_items"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewGallery)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        loadGalleryItems()

        adapter = GalleryAdapter(requireContext(), galleryItems, this)
        recyclerView.adapter = adapter

        val addButton: FloatingActionButton = view.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            openImagePicker()
        }

        return view
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onEditNameClick(position: Int) {
        selectedPosition = position
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_name_gallery, null)
        val editText = dialogView.findViewById<EditText>(R.id.editTextName)
        editText.setText(galleryItems[position].name)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Name")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val newName = editText.text.toString()
                galleryItems[position].name = newName
                adapter.updateItem(position, galleryItems[position])
                saveGalleryItems()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDeleteItemClick(position: Int) {
        val deletedItem = adapter.deleteItem(position)
        saveGalleryItems()

        Snackbar.make(requireView(), "Item deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                galleryItems.add(position, deletedItem)
                adapter.notifyItemInserted(position)
                saveGalleryItems()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri? = data?.data
            selectedImage?.let {
                val existingItem = galleryItems.find { item -> item.imageUri == it.toString() }
                if (existingItem == null) {
                    val newItem = GalleryItem(it.toString(), "Galeri Baru")
                    adapter.addItem(newItem)
                    saveGalleryItems()
                } else {
                    Snackbar.make(requireView(), "Item already exists", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun saveGalleryItems() {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val jsonArray = JSONArray()
        for (item in galleryItems) {
            val jsonObject = JSONObject()
            jsonObject.put("imageUri", item.imageUri)
            jsonObject.put("name", item.name)
            jsonArray.put(jsonObject)
        }

        editor.putString(KEY_GALLERY_ITEMS, jsonArray.toString())
        editor.apply()
    }

    private fun loadGalleryItems() {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(KEY_GALLERY_ITEMS, null)

        if (json != null) {
            val jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val imageUri = jsonObject.getString("imageUri")
                val name = jsonObject.getString("name")
                galleryItems.add(GalleryItem(imageUri, name))
            }
        }
    }
}
