package com.example.dongi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.dongi.api.LoginRequest
import com.example.dongi.api.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    // Declare the views
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

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

        // Initialize the views
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        // Set up the login button click listener
        loginButton.setOnClickListener {
            if (validateInputs()) {
                loginUser()
            }
        }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        val loginRequest = LoginRequest(email, password)

        RetrofitClient.instance.login(loginRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    Log.d("API Response", "Response Body: $responseBody")

                    if (responseBody != null) {
                        // Process the raw response if needed
                        Toast.makeText(this@LoginActivity, "ورود با موفقیت انجام شد", Toast.LENGTH_SHORT).show()
                        // Navigate to another activity if needed
                        val intent = Intent(this@LoginActivity, GroupsActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Login failed
                        Toast.makeText(this@LoginActivity, "ورود ناموفق بود", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Log the raw response body for debugging
                    val errorBody = response.errorBody()?.string()
                    Log.e("API Error", "Error Body: $errorBody")
                    Toast.makeText(this@LoginActivity, "ورود ناموفق بود", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("API Error", "Error: ${t.message}")
                Toast.makeText(this@LoginActivity, "ورود ناموفق بود: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun handleBackPress() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateInputs(): Boolean {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty()) {
            emailEditText.error = "ایمیل الزامی است"
            emailEditText.requestFocus()
            return false
        }

        if (!isValidEmail(email)) {
            emailEditText.error = "ایمیل نامعتبر است"
            emailEditText.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            passwordEditText.error = "رمز عبور الزامی است"
            passwordEditText.requestFocus()
            return false
        }

        if (!isValidPassword(password)) {
            passwordEditText.error = "گذرواژه باید حداقل ۶ کاراکتر باشد"
            passwordEditText.requestFocus()
            return false
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        )
        return emailPattern.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}