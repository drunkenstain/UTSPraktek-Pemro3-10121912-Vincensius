package com.example.uts.daily

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uts.R

class FriendListAdapter(
    private val context: Context,
    private val items: MutableList<FriendlistFragment.FriendListItem>,
    private val listener: FriendItemClickListener
) : RecyclerView.Adapter<FriendListAdapter.ViewHolder>() {

    interface FriendItemClickListener {
        fun onEditNameClick(position: Int)
        fun onAddFriendClick()
        fun onDeleteFriendClick(position: Int)
        fun onItemDeleted(position: Int, item: FriendlistFragment.FriendListItem)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val textView: TextView = view.findViewById(R.id.name_friend)
        val editNameButton: ImageView = view.findViewById(R.id.editNameButton)
        val deleteButton: ImageView = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.image_friend_text, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        Glide.with(context)
            .load(item.imageUri)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.imageView)

        holder.textView.text = item.name

        holder.editNameButton.setOnClickListener {
            listener.onEditNameClick(position)
        }

        holder.deleteButton.setOnClickListener {
            listener.onDeleteFriendClick(position)
            listener.onItemDeleted(position, item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun deleteFriendItem(position: Int): FriendlistFragment.FriendListItem {
        val deletedItem = items.removeAt(position)
        notifyDataSetChanged()
        return deletedItem
    }

    fun restoreItem(position: Int, item: FriendlistFragment.FriendListItem) {
        items.add(position, item)
        notifyDataSetChanged()
    }

    fun addFriend(friend: FriendlistFragment.FriendListItem) {
        items.add(friend)
        notifyDataSetChanged()
    }

    fun editName(position: Int, newName: String) {
        items[position].name = newName
        notifyDataSetChanged()
    }

    fun changeImage(position: Int, imageUri: Uri) {
        items[position].imageUri = imageUri.toString()
        notifyDataSetChanged()
    }
}
