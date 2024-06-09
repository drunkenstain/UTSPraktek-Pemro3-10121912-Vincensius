package com.example.uts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "isi username atau password", Toast.LENGTH_SHORT).show()
            } else {
                val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                val registeredUsername = sharedPref.getString("username", null)
                val registeredPassword = sharedPref.getString("password", null)

                if (username == registeredUsername && password == registeredPassword) {
                    with(sharedPref.edit()) {
                        putBoolean("isLoggedIn", true)
                        apply()
                    }

                    Toast.makeText(this, "Login sukses", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "username atau password salah", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
