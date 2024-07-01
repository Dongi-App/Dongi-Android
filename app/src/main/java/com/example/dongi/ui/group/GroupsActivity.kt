package com.example.dongi.ui.group

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dongi.R
import com.example.dongi.api.GroupsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.dongi.api.RetrofitClient

class GroupsActivity : AppCompatActivity() {

    private lateinit var groupsRecyclerView: RecyclerView
    private lateinit var groupsAdapter: GroupsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)

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

        groupsRecyclerView = findViewById(R.id.groupsRecyclerView)
        groupsRecyclerView.layoutManager = LinearLayoutManager(this)

        val addGroupImageView: ImageView = findViewById(R.id.addGroupImageView)
        addGroupImageView.setOnClickListener {
            val intent = Intent(this, AddGroupActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        fetchGroups()
    }

    private fun fetchGroups() {
        RetrofitClient.getInstance(this).getGroups().enqueue(object : Callback<GroupsResponse> {
            override fun onResponse(call: Call<GroupsResponse>, response: Response<GroupsResponse>) {
                if (response.isSuccessful) {
                    val groupsResponse = response.body()
                    if (groupsResponse != null) {
                        groupsAdapter = GroupsAdapter(this@GroupsActivity, groupsResponse.groups)
                        groupsRecyclerView.adapter = groupsAdapter
                    } else {
                        Toast.makeText(this@GroupsActivity, "دریافت گروه‌ها با مشکل مواجه شد", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@GroupsActivity, "Failed to load groups", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GroupsResponse>, t: Throwable) {
                Log.e("API Error", "Error: ${t.message}")
                Toast.makeText(this@GroupsActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}