package com.example.uts.other

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.uts.R

class MediaAdapter(
    private val context: Context,
    private val mediaList: MutableList<MediaFragment.MediaItem>,
    private val deleteListener: OnDeleteClickListener
) : RecyclerView.Adapter<MediaAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)
        val videoView: VideoView = view.findViewById(R.id.videoView)
        var isPlaying = false

        init {
            buttonDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    deleteListener.onDeleteClick(position)
                }
            }
            videoView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (isPlaying) {
                        videoView.pause()
                        isPlaying = false
                    } else {
                        videoView.start()
                        isPlaying = true
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_media, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val media = mediaList[position]

        if (media.isVideo) {
            holder.videoView.visibility = View.VISIBLE
            holder.videoView.setVideoURI(Uri.parse(media.mediaUri))
            holder.videoView.seekTo(1) // Load the first frame as a thumbnail
        } else {
            holder.videoView.visibility = View.GONE
        }

        holder.textViewTitle.text = media.title
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    fun addItem(item: MediaFragment.MediaItem) {
        mediaList.add(item)
        notifyItemInserted(mediaList.size - 1)
    }

    fun deleteItem(position: Int): MediaFragment.MediaItem {
        val deletedItem = mediaList.removeAt(position)
        notifyItemRemoved(position)
        return deletedItem
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }
}
