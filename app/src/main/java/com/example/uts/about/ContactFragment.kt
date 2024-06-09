package com.example.uts.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.uts.R

class ContactFragment : Fragment() {

    private lateinit var editTextName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextInstagram: EditText
    private lateinit var buttonSaveContact: Button
    private lateinit var buttonViewContact: Button
    private lateinit var contactDetails: LinearLayout
    private lateinit var textViewName: TextView
    private lateinit var textViewPhone: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var textViewInstagram: TextView

    private val PREFS_NAME = "contact_prefs"
    private val KEY_NAME = "name"
    private val KEY_PHONE = "phone"
    private val KEY_EMAIL = "email"
    private val KEY_INSTAGRAM = "instagram"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        editTextName = view.findViewById(R.id.editTextName)
        editTextPhone = view.findViewById(R.id.editTextPhone)
        editTextEmail = view.findViewById(R.id.editTextEmail)
        editTextInstagram = view.findViewById(R.id.editTextInstagram)
        buttonSaveContact = view.findViewById(R.id.buttonSaveContact)
        buttonViewContact = view.findViewById(R.id.buttonViewContact)
        contactDetails = view.findViewById(R.id.contactDetails)
        textViewName = view.findViewById(R.id.textViewName)
        textViewPhone = view.findViewById(R.id.textViewPhone)
        textViewEmail = view.findViewById(R.id.textViewEmail)
        textViewInstagram = view.findViewById(R.id.textViewInstagram)

        buttonSaveContact.setOnClickListener {
            saveContact()
        }

        buttonViewContact.setOnClickListener {
            viewContact()
        }

        textViewPhone.setOnClickListener {
            val phone = textViewPhone.text.toString()
            if (phone.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                startActivity(intent)
            }
        }

        textViewEmail.setOnClickListener {
            val email = textViewEmail.text.toString()
            if (email.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
                startActivity(intent)
            }
        }

        textViewInstagram.setOnClickListener {
            val instagramUrl = textViewInstagram.text.toString()
            if (instagramUrl.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))
                startActivity(intent)
            }
        }

        return view
    }

    private fun saveContact() {
        val name = editTextName.text.toString().trim()
        val phone = editTextPhone.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val instagram = editTextInstagram.text.toString().trim()

        if (name.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && instagram.isNotEmpty()) {
            val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(KEY_NAME, name)
            editor.putString(KEY_PHONE, phone)
            editor.putString(KEY_EMAIL, email)
            editor.putString(KEY_INSTAGRAM, instagram)
            editor.apply()

            editTextName.text.clear()
            editTextPhone.text.clear()
            editTextEmail.text.clear()
            editTextInstagram.text.clear()
        }
    }

    private fun viewContact() {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val name = sharedPreferences.getString(KEY_NAME, "")
        val phone = sharedPreferences.getString(KEY_PHONE, "")
        val email = sharedPreferences.getString(KEY_EMAIL, "")
        val instagram = sharedPreferences.getString(KEY_INSTAGRAM, "")

        if (name!!.isNotEmpty() && phone!!.isNotEmpty() && email!!.isNotEmpty() && instagram!!.isNotEmpty()) {
            textViewName.text = name
            textViewPhone.text = phone
            textViewEmail.text = email
            textViewInstagram.text = instagram
            contactDetails.visibility = View.VISIBLE
        } else {
            contactDetails.visibility = View.GONE
        }
    }
}
