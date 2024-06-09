package com.example.uts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.uts.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val imageView = findViewById<ImageView>(R.id.icon)
        imageView.startAnimation(fadeInAnimation)

        fadeInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

                if (isLoggedIn) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
                finish()
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
}
