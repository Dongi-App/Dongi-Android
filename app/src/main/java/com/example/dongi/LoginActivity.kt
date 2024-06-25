package com.example.dongi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        // Set the navigation icon color
        val navIcon = toolbar.navigationIcon
        if (navIcon != null) {
            val wrappedDrawable = DrawableCompat.wrap(navIcon)
            DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(this, R.color.secondaryButtonColor))
            toolbar.navigationIcon = wrappedDrawable
        }

        toolbar.setNavigationOnClickListener {
            handleBackPress()
        }

        // Registering a callback for the back press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress()
            }
        })

        // Existing code for handling login logic
        val loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            // Handle login logic
        }
    }

    private fun handleBackPress() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}