package com.example.dongi.ui.group

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        groupsRecyclerView = findViewById(R.id.groupsRecyclerView)
        groupsRecyclerView.layoutManager = LinearLayoutManager(this)

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
                        Toast.makeText(this@GroupsActivity, "Failed to load groups", Toast.LENGTH_SHORT).show()
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