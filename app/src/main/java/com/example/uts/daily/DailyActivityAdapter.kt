package com.example.uts.daily

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uts.R
import com.example.uts.daily.DailyFragment.DailyItem
import com.google.gson.Gson

class DailyActivityAdapter(
    private val context: Context,
    private val activities: MutableList<DailyItem>,
    private val deletedItemsSet: MutableSet<String>,
    private val sharedPreferences: SharedPreferences
) : RecyclerView.Adapter<DailyActivityAdapter.DailyActivityViewHolder>() {

    private val gson = Gson()

    class DailyActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.daily_title)
        val description: TextView = view.findViewById(R.id.daily_description)
        val image: ImageView = view.findViewById(R.id.daily_image)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daily_activity, parent, false)
        return DailyActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.title.text = activity.title
        holder.description.text = activity.description
        holder.image.setImageResource(activity.imageResource)
        holder.deleteButton.setOnClickListener {
            deleteActivity(activity.id)
            activities.remove(activity)
            notifyDataSetChanged()
        }
        if (deletedItemsSet.contains(activity.id)) {
            holder.itemView.visibility = View.GONE
        } else {
            holder.itemView.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = activities.size

    fun addActivity(activity: DailyItem) {
        activities.add(0, activity)
        saveActivities()
        notifyDataSetChanged()
    }

    private fun deleteActivity(id: String) {
        deletedItemsSet.add(id)
        saveActivities()
    }

    private fun saveActivities() {
        val editor = sharedPreferences.edit()
        val activitiesJson = gson.toJson(activities)
        editor.putString("activities", activitiesJson)
        val deletedItemsJson = gson.toJson(deletedItemsSet)
        editor.putString("deletedItems", deletedItemsJson)
        editor.apply()
    }
}
