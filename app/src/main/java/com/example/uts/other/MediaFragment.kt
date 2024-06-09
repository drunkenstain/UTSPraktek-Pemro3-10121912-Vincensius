package com.example.uts.other

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uts.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONObject

class MediaFragment : Fragment(), MediaAdapter.OnDeleteClickListener {

    data class MediaItem(var title: String, var mediaUri: String, var isVideo: Boolean)

    private lateinit var adapter: MediaAdapter
    private val mediaList = mutableListOf<MediaItem>()
    private lateinit var recyclerView: RecyclerView
    private val PREFS_NAME = "media_prefs"
    private val KEY_MEDIA_ITEMS = "media_items"
    private val ADD_MEDIA_REQUEST = 1
    private val REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_media, container, false)

        checkAndRequestPermissions()

        recyclerView = view.findViewById(R.id.recyclerViewMedia)
        recyclerView.layoutManager = LinearLayoutManager(context)

        loadMediaItems()

        adapter = MediaAdapter(requireContext(), mediaList, this)
        recyclerView.adapter = adapter

        val addMusicButton: FloatingActionButton = view.findViewById(R.id.addMusicButton)
        addMusicButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "audio/*"
            startActivityForResult(intent, ADD_MEDIA_REQUEST)
        }

        val addVideoButton: FloatingActionButton = view.findViewById(R.id.addVideoButton)
        addVideoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "video/*"
            startActivityForResult(intent, ADD_MEDIA_REQUEST)
        }

        return view
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted
            } else {
                // Permission denied
                Snackbar.make(requireView(), "Permission denied to read your External storage", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_MEDIA_REQUEST && resultCode == Activity.RESULT_OK) {
            val selectedMedia: Uri? = data?.data
            selectedMedia?.let { uri ->
                val existingMedia = mediaList.find { item -> item.mediaUri == uri.toString() }
                if (existingMedia == null) {
                    showTitleInputDialog(uri)
                } else {
                    Snackbar.make(requireView(), "Media sudah ada", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showTitleInputDialog(uri: Uri) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Masukkan Judul")

        val input = EditText(requireContext())
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val title = input.text.toString().trim()
            if (title.isNotEmpty()) {
                val newItem = MediaItem(title, uri.toString(), uri.toString().endsWith(".mp4"))
                adapter.addItem(newItem)
                saveMediaItems()
                dialog.dismiss()
            } else {
                Snackbar.make(requireView(), "Judul tidak boleh kosong", Snackbar.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Batal") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun saveMediaItems() {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val jsonArray = JSONArray()
        for (item in mediaList) {
            val jsonObject = JSONObject()
            jsonObject.put("mediaUri", item.mediaUri)
            jsonObject.put("title", item.title)
            jsonObject.put("isVideo", item.isVideo)
            jsonArray.put(jsonObject)
        }

        editor.putString(KEY_MEDIA_ITEMS, jsonArray.toString())
        editor.apply()
    }

    private fun loadMediaItems() {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(KEY_MEDIA_ITEMS, null)

        if (json != null) {
            val jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val mediaUri = jsonObject.getString("mediaUri")
                val title = jsonObject.getString("title")
                val isVideo = jsonObject.getBoolean("isVideo")
                mediaList.add(MediaItem(title, mediaUri, isVideo))
            }
        }
    }

    override fun onDeleteClick(position: Int) {
        val deletedItem = adapter.deleteItem(position)
        saveMediaItems()
        Snackbar.make(requireView(), "${if (deletedItem.isVideo) "Video" else "Musik"} dihapus", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                mediaList.add(position, deletedItem)
                adapter.notifyItemInserted(position)
                saveMediaItems()
            }
            .show()
    }
}
