package com.example.dongi.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.dongi.R
import com.example.dongi.ui.group.GroupsActivity
import com.example.dongi.ui.profile.ProfileActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var profileButton: Button
    private lateinit var groupsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        profileButton = findViewById(R.id.profileButton)
        groupsButton = findViewById(R.id.groupsButton)

        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        groupsButton.setOnClickListener {
            val intent = Intent(this, GroupsActivity::class.java)
            startActivity(intent)
        }
    }
}
