package com.example.uts.home

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.uts.R

class InterestFragment : Fragment() {

    private lateinit var interestTextView: TextView

    private val PREFS_NAME = "interest_prefs"
    private val INTEREST_KEY = "interest"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_interest, container, false)

        interestTextView = view.findViewById(R.id.interest_text_view)

        loadInterestData()

        interestTextView.setOnClickListener {
            showEditInterestDialog()
        }

        return view
    }

    private fun showEditInterestDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_edit_interest, null)
        val editInterest = dialogLayout.findViewById<EditText>(R.id.edit_interest)

        editInterest.setText(interestTextView.text.toString())

        builder.setView(dialogLayout)
        builder.setPositiveButton("Save") { dialog, which ->
            val newInterest = editInterest.text.toString()
            interestTextView.text = newInterest
            saveInterestData(newInterest)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun saveInterestData(interest: String) {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(INTEREST_KEY, interest)
        editor.apply()
    }

    private fun loadInterestData() {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val interest = sharedPreferences.getString(INTEREST_KEY, "Your Interest")
        interestTextView.text = interest
    }
}
