package com.example.uts.daily

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uts.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FriendlistFragment : Fragment(), FriendListAdapter.FriendItemClickListener {

    data class FriendListItem(var imageUri: String, var name: String)

    private lateinit var adapter: FriendListAdapter
    private val friendListItems = mutableListOf<FriendListItem>()
    private var selectedPosition: Int? = null
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friend_list, container, false)

        sharedPreferences = requireContext().getSharedPreferences("friendlist_prefs", Context.MODE_PRIVATE)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        loadFriends()

        adapter = FriendListAdapter(requireContext(), friendListItems, this)
        recyclerView.adapter = adapter

        val addButton: FloatingActionButton = view.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            onAddFriendClick()
        }

        return view
    }

    private fun saveFriends() {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(friendListItems)
        editor.putString("friend_list", json)
        editor.apply()
    }

    private fun loadFriends() {
        val json = sharedPreferences.getString("friend_list", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<FriendListItem>>() {}.type
            friendListItems.addAll(gson.fromJson(json, type))
        }
    }

    override fun onEditNameClick(position: Int) {
        selectedPosition = position
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_name, null)
        val editText = dialogView.findViewById<TextInputEditText>(R.id.editTextName)
        val buttonChangeImage = dialogView.findViewById<Button>(R.id.buttonChangeImage)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Name")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val newName = editText.text.toString()
                adapter.editName(position, newName)
                saveFriends()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

        buttonChangeImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    override fun onItemDeleted(position: Int, item: FriendListItem) {
        recycleBin.add(item)
        Snackbar.make(requireView(), "Item deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                undoDelete(position)
            }
            .show()
    }

    override fun onAddFriendClick() {
        val friendImages = listOf(
            "android.resource://com.example.uts/drawable/friend1",
            "android.resource://com.example.uts/drawable/friend2",
            "android.resource://com.example.uts/drawable/friend3",
            "android.resource://com.example.uts/drawable/friend4",
            "android.resource://com.example.uts/drawable/friend5"
        )
        val randomImageUri = friendImages.random()
        val newFriend = FriendListItem(randomImageUri, "Teman Baru")
        adapter.addFriend(newFriend)
        saveFriends()
    }

    private val recycleBin = mutableListOf<FriendListItem>()

    override fun onDeleteFriendClick(position: Int) {
        val deletedItem = adapter.deleteFriendItem(position)
        recycleBin.add(deletedItem)
        Snackbar.make(requireView(), "Item deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                undoDelete(position)
            }
            .show()
        saveFriends()
    }

    private fun undoDelete(position: Int) {
        val deletedItem = recycleBin.removeAt(recycleBin.lastIndex)
        adapter.restoreItem(position, deletedItem)
        saveFriends()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri? = data?.data
            selectedImage?.let {
                selectedPosition?.let { pos ->
                    adapter.changeImage(pos, it)
                    saveFriends()
                }
            }
        }
    }
}
