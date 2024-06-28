package com.example.dongi.ui.signup

import android.content.Context
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
import com.example.dongi.R
import com.example.dongi.ui.splash.WelcomeActivity
import com.example.dongi.api.RetrofitClient
import com.example.dongi.api.SignupRequest
import com.example.dongi.api.SignupResponse
import com.example.dongi.ui.group.GroupsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    // Declare the views
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        // Set the navigation icon color
        val navIcon = toolbar.navigationIcon
        if (navIcon != null) {
            val wrappedDrawable = DrawableCompat.wrap(navIcon)
            DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(this,
                R.color.secondaryButtonColor
            ))
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
        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)

        // Set up the register button click listener
        registerButton.setOnClickListener {
            if (validateInputs()) {
                signUpUser()
            }
        }
    }

    private fun signUpUser() {
        val firstName = firstNameEditText.text.toString().trim()
        val lastName = lastNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        val signupRequest = SignupRequest(firstName, lastName, email, password)

        RetrofitClient.getInstance(this).signup(signupRequest).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    Log.d("API Response", "Response Body: ${signupResponse}")

                    if (signupResponse?.token != null) {
                        // Store the token in SharedPreferences
                        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("AUTH_TOKEN", signupResponse.token)
                        editor.apply()
                        // Sign-up successful
                        Toast.makeText(this@SignUpActivity, "ثبت نام با موفقیت انجام شد", Toast.LENGTH_SHORT).show()
                        // Navigate to another activity if needed
                        val intent = Intent(this@SignUpActivity, GroupsActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Sign-up failed
                        Toast.makeText(this@SignUpActivity, "ثبت نام ناموفق بود", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Log the raw response body for debugging
                    val errorBody = response.errorBody()?.string()
                    Log.e("API Error", "Error Body: $errorBody")
                    Toast.makeText(this@SignUpActivity, "ثبت نام ناموفق بود", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Log.e("API Error", "Error: ${t.message}")
                Toast.makeText(this@SignUpActivity, "ثبت نام ناموفق بود: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleBackPress() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateInputs(): Boolean {
        val firstName = firstNameEditText.text.toString().trim()
        val lastName = lastNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (firstName.isEmpty()) {
            firstNameEditText.error = "نام الزامی است"
            firstNameEditText.requestFocus()
            return false
        }

        if (lastName.isEmpty()) {
            lastNameEditText.error = "نام خانوادگی الزامی است"
            lastNameEditText.requestFocus()
            return false
        }

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
            passwordEditText.error = "گذرواژه باید حداقل 6 کاراکتر باشد"
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