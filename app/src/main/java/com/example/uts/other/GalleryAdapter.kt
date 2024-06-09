package com.example.uts.gallery

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
import com.example.uts.other.GalleryFragment

class GalleryAdapter(
    private val context: Context,
    private val items: MutableList<GalleryFragment.GalleryItem>,
    private val listener: GalleryItemClickListener
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    interface GalleryItemClickListener {
        fun onEditNameClick(position: Int)
        fun onDeleteItemClick(position: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.gallery_image)
        val textView: TextView = view.findViewById(R.id.name_text)
        val editButton: ImageView = view.findViewById(R.id.edit_button)
        val deleteButton: ImageView = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Load image using Glide
        Glide.with(context)
            .load(Uri.parse(item.imageUri))
            .placeholder(R.drawable.ic_launcher_background) // Placeholder image
            .into(holder.imageView)

        holder.textView.text = item.name

        holder.editButton.setOnClickListener {
            listener.onEditNameClick(position)
        }

        holder.deleteButton.setOnClickListener {
            listener.onDeleteItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: GalleryFragment.GalleryItem) {
        items.add(item)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int): GalleryFragment.GalleryItem {
        val removedItem = items.removeAt(position)
        notifyDataSetChanged()
        return removedItem
    }

    fun updateItem(position: Int, newItem: GalleryFragment.GalleryItem) {
        items[position] = newItem
        notifyDataSetChanged()
    }
}
