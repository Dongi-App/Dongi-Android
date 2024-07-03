package com.example.dongi.ui.group

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dongi.R
import com.example.dongi.api.AddGroupResponse
import com.example.dongi.api.LeaveGroupRequest
import com.example.dongi.api.RetrofitClient
import com.example.dongi.ui.invite.SendInvitationActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupDetailsActivity : AppCompatActivity() {

    private lateinit var membersRecyclerView: RecyclerView
    private lateinit var groupMembersAdapter: GroupMembersAdapter
    private lateinit var groupTitleTV: TextView
    private lateinit var addMemberLayout: RelativeLayout
    private lateinit var leaveGroupLayout: RelativeLayout
    private lateinit var addExpLayout: RelativeLayout
    private lateinit var expListLayout: RelativeLayout
    private lateinit var groupId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_details)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        groupId = intent.getStringExtra("GROUP_ID") ?: return
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

        groupTitleTV = findViewById(R.id.groupTitleTextView)
        expListLayout = findViewById(R.id.exp_list)
        addMemberLayout = findViewById(R.id.add_member)
        leaveGroupLayout = findViewById(R.id.leave_group)
        addExpLayout = findViewById(R.id.add_exp)

        expListLayout.setOnClickListener {
            val intent = Intent(this, ExpenseListActivity::class.java).apply {
                putExtra("GROUP_ID", groupId)
            }
            startActivity(intent)
        }
        addMemberLayout.setOnClickListener {
            val intent = Intent(this,SendInvitationActivity::class.java).apply {
                putExtra("GROUP_ID", groupId)
            }
            startActivity(intent)
        }
        leaveGroupLayout.setOnClickListener {
            groupLeave()
        }
        addExpLayout.setOnClickListener {
            val intent = Intent(this, ExpenseActivity::class.java).apply {
                putExtra("GROUP_ID", groupId)
            }
            startActivity(intent)
        }

        membersRecyclerView = findViewById(R.id.groupsMembersRecyclerView)
        membersRecyclerView.layoutManager = LinearLayoutManager(this)

        val addGroupImageView: ImageView = findViewById(R.id.add_user)
        addGroupImageView.setOnClickListener {
            val intent = Intent(this, GroupDetailsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        fetchMembersData()
    }

    private fun groupLeave() {
        RetrofitClient.getInstance(this@GroupDetailsActivity).leaveGroup(LeaveGroupRequest(groupId)).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    finish()
                } else {
                    Toast.makeText(this@GroupDetailsActivity, "Failed to leave group ", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API Error", "Error: ${t.message}")
                Toast.makeText(this@GroupDetailsActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchMembersData() {

        RetrofitClient.getInstance(this@GroupDetailsActivity).getGroupDetails(groupId).enqueue(object : Callback<AddGroupResponse> {
            override fun onResponse(call: Call<AddGroupResponse>, response: Response<AddGroupResponse>) {
                if (response.isSuccessful) {
                    val groupsResponse = response.body()
                    if (groupsResponse != null) {
                        groupTitleTV.text = groupsResponse.group.name
                        groupMembersAdapter = GroupMembersAdapter(this@GroupDetailsActivity, groupsResponse.group.members)
                        membersRecyclerView.adapter = groupMembersAdapter
                    } else {
                        Toast.makeText(this@GroupDetailsActivity, "دریافت اطلاعات گروه‌ با مشکل مواجه شد", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@GroupDetailsActivity, "Failed to load group details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddGroupResponse>, t: Throwable) {
                Log.e("API Error", "Error: ${t.message}")
                Toast.makeText(this@GroupDetailsActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}