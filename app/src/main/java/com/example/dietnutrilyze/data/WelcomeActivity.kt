package com.example.dietnutrilyze.data

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dietnutrilyze.R
import androidx.appcompat.widget.AppCompatButton
import android.content.Intent

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Initialize buttons
        val loginButton: AppCompatButton = findViewById(R.id.login_button)
        val signUpButton: AppCompatButton = findViewById(R.id.sign_up_button)

        // Set onClick listeners
        loginButton.setOnClickListener {
            // Navigate to LoginActivity
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        signUpButton.setOnClickListener {
            // Navigate to SignUpActivity
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }
    }
}