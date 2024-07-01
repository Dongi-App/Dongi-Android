package com.example.dongi.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.dongi.R
import com.example.dongi.api.RetrofitClient
import com.example.dongi.api.UserDataResponse
import com.example.dongi.ui.splash.WelcomeActivity
import com.example.dongi.util.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileNameTextView: TextView
    private lateinit var profileEmailTextView: TextView
    private lateinit var editProfileLayout: RelativeLayout
    private lateinit var logoutLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

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

        profileNameTextView = findViewById(R.id.profileNameTextView)
        profileEmailTextView = findViewById(R.id.profileEmailTextView)
        editProfileLayout = findViewById(R.id.editProfile)
        logoutLayout = findViewById(R.id.logout)

        editProfileLayout.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        logoutLayout.setOnClickListener {
            logoutUser()
        }
    }

    override fun onResume() {
        super.onResume()
        fetchUserData()
    }

    private fun fetchUserData() {
        val call = RetrofitClient.getInstance(this).getUserData()

        call.enqueue(object : Callback<UserDataResponse> {
            override fun onResponse(call: Call<UserDataResponse>, response: Response<UserDataResponse>) {
                if (response.isSuccessful) {
                    val userData = response.body()?.user
                    if (userData != null) {
                        val fullName = "${userData.first_name} ${userData.last_name}"
                        profileNameTextView.text = fullName
                        profileEmailTextView.text = userData.email
                    } else {
                        Toast.makeText(this@ProfileActivity, "دریافت اطلاعات کاربر با مشکل مواجه شده است.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ProfileActivity, "دریافت اطلاعات کاربر با مشکل مواجه شده است.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "دریافت اطلاعات کاربر با مشکل مواجه شده است.: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun logoutUser() {
        val call = RetrofitClient.getInstance(this).logoutUser()

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    SharedPreferencesHelper.clearToken(this@ProfileActivity)
                    val intent = Intent(this@ProfileActivity, WelcomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@ProfileActivity, "خروج با مشکل مواجه شده است. دوباره تلاش کنید.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "خروج با مشکل مواجه شده است.: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
