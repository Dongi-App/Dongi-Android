package com.example.dongi.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dongi.R
import com.example.dongi.api.RetrofitClient
import com.example.dongi.ui.group.GroupsActivity
import com.example.dongi.ui.invite.InvitationListActivity
import com.example.dongi.ui.profile.ProfileActivity
import com.example.dongi.ui.splash.WelcomeActivity
import com.example.dongi.util.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    private lateinit var profileButton: Button
    private lateinit var groupsButton: Button
    private lateinit var inviteButton: Button
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        profileButton = findViewById(R.id.profileButton)
        groupsButton = findViewById(R.id.groupsButton)
        inviteButton = findViewById(R.id.inviteButton)
        logoutButton = findViewById(R.id.logoutButton)

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        groupsButton.setOnClickListener {
            val intent = Intent(this, GroupsActivity::class.java)
            startActivity(intent)
        }

        inviteButton.setOnClickListener {
            val intent = Intent(this, InvitationListActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        val call = RetrofitClient.getInstance(this).logoutUser()

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    SharedPreferencesHelper.clearToken(this@DashboardActivity)
                    val intent = Intent(this@DashboardActivity, WelcomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@DashboardActivity, "خروج با مشکل مواجه شده است. دوباره تلاش کنید.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "خروج با مشکل مواجه شده است.: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
