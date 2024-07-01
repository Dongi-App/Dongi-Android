package com.example.dongi.ui.profile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.dongi.R
import com.example.dongi.api.RetrofitClient
import com.example.dongi.api.UserDataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var registerChangesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

        // Handle the navigation click to go back to the previous activity
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        registerChangesButton = findViewById(R.id.registerChangesButton)

        fetchUserData()

        registerChangesButton.setOnClickListener {
            updateProfile()
        }
    }

    private fun fetchUserData() {
        val call = RetrofitClient.getInstance(this).getUserData()

        call.enqueue(object : Callback<UserDataResponse> {
            override fun onResponse(call: Call<UserDataResponse>, response: Response<UserDataResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()?.user
                    if (user != null) {
                        firstNameEditText.setText(user.first_name)
                        lastNameEditText.setText(user.last_name)
                    } else {
                        Toast.makeText(this@EditProfileActivity, "دریافت اطلاعات کاربر ناموفق بود", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@EditProfileActivity, "دریافت اطلاعات کاربر ناموفق بود", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                Toast.makeText(this@EditProfileActivity, "خطایی رخ داد: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateProfile() {
        val firstName = firstNameEditText.text.toString().trim()
        val lastName = lastNameEditText.text.toString().trim()

        if (firstName.isEmpty() && lastName.isEmpty()) {
            Toast.makeText(this, "لطفاً حداقل یکی از فیلدها را پر کنید", Toast.LENGTH_SHORT).show()
            return
        }

        val request = mutableMapOf<String, String>()
        if (firstName.isNotEmpty()) request["first_name"] = firstName
        if (lastName.isNotEmpty()) request["last_name"] = lastName

        val call = RetrofitClient.getInstance(this).updateUserData(request)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditProfileActivity, "اطلاعات حساب کاربری با موفقیت به روز شد", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditProfileActivity, "بروزرسانی اطلاعات حساب کاربری ناموفق بود", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@EditProfileActivity, "خطایی رخ داد: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
