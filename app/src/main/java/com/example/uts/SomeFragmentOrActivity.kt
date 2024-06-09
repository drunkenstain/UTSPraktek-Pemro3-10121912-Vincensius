package com.example.uts

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.uts.about.ContactFragment
import com.example.uts.about.FindmeFragment

class SomeFragmentOrActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_some_fragment_or_activity)

        val someButton = findViewById<Button>(R.id.someButton)
        someButton.setOnClickListener {
            loadFragment(ContactFragment())
        }

        val findMeButton = findViewById<Button>(R.id.findMeButton)
        findMeButton.setOnClickListener {
            loadFragment(FindmeFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
