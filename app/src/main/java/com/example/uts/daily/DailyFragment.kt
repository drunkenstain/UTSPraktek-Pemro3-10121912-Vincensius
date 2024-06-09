package com.example.uts.daily

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uts.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class DailyFragment : Fragment() {

    data class DailyItem(val id: String, val title: String, val description: String, val imageResource: Int)

    private lateinit var adapter: DailyActivityAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private val activities = mutableListOf<DailyItem>()
    private val deletedItemsSet = mutableSetOf<String>()
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("daily_activities", Context.MODE_PRIVATE)
        loadActivities()

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = DailyActivityAdapter(requireContext(), activities, deletedItemsSet, sharedPreferences)
        recyclerView.adapter = adapter

        adjustRecyclerViewHeight()

        val editTextTitle: EditText = view.findViewById(R.id.editTextActivityTitle)
        val editTextDescription: EditText = view.findViewById(R.id.editTextActivityDescription)
        val buttonAddActivity: FloatingActionButton = view.findViewById(R.id.buttonAddActivity)

        buttonAddActivity.setOnClickListener {
            val title = editTextTitle.text.toString().trim()
            val description = editTextDescription.text.toString().trim()
            if (title.isNotEmpty() && description.isNotEmpty()) {
                val imageResource = getImageResource(title)
                val newActivity = DailyItem(UUID.randomUUID().toString(), title, description, imageResource)

                adapter.addActivity(newActivity)
                saveActivities()
                editTextTitle.text.clear()
                editTextDescription.text.clear()
                recyclerView.scrollToPosition(0)
                adjustRecyclerViewHeight()
            } else {
                Toast.makeText(requireContext(), "Judul atau deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun adjustRecyclerViewHeight() {
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            var totalHeight = 0
            for (i in 0 until totalItemCount) {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(i)
                if (viewHolder != null) {
                    totalHeight += viewHolder.itemView.height
                }
            }

            val screenHeight = resources.displayMetrics.heightPixels

            if (totalHeight < screenHeight) {
                recyclerView.layoutParams.height = totalHeight
                recyclerView.isNestedScrollingEnabled = false
            } else {
                recyclerView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                recyclerView.isNestedScrollingEnabled = true
            }

            recyclerView.requestLayout()
        }
    }

    private fun loadActivities() {
        val activitiesJson = sharedPreferences.getString("activities", null)
        if (!activitiesJson.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<DailyItem>>() {}.type
            val loadedActivities: MutableList<DailyItem> = gson.fromJson(activitiesJson, type)
            activities.clear()
            activities.addAll(loadedActivities)
        }

        val deletedItemsJson = sharedPreferences.getString("deletedItems", null)
        if (!deletedItemsJson.isNullOrEmpty()) {
            val type = object : TypeToken<MutableSet<String>>() {}.type
            val loadedDeletedItems: MutableSet<String> = gson.fromJson(deletedItemsJson, type)
            deletedItemsSet.clear()
            deletedItemsSet.addAll(loadedDeletedItems)
        }
    }

    private fun saveActivities() {
        val editor = sharedPreferences.edit()
        val activitiesJson = gson.toJson(activities)
        editor.putString("activities", activitiesJson)
        val deletedItemsJson = gson.toJson(deletedItemsSet)
        editor.putString("deletedItems", deletedItemsJson)
        editor.apply()
    }

    private fun getImageResource(title: String): Int {
        val letter = title.trim().firstOrNull()?.toLowerCase() ?: 'a'
        return when (letter) {
            in 'a'..'z' -> resources.getIdentifier("letter_$letter", "drawable", requireContext().packageName)
            else -> R.drawable.ic_default
        }
    }
}
