package com.example.dongi.ui.group

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.dongi.R
import com.example.dongi.api.AddGroupRequest
import com.example.dongi.api.AddGroupResponse
import com.example.dongi.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddGroupActivity : AppCompatActivity() {

    private lateinit var groupNameEditText: EditText
    private lateinit var addGroupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

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

        groupNameEditText = findViewById(R.id.groupNameEditText)
        addGroupButton = findViewById(R.id.addGroupButton)

        addGroupButton.setOnClickListener {
            val groupName = groupNameEditText.text.toString().trim()
            if (groupName.isNotEmpty()) {
                addGroup(groupName)
            } else {
                groupNameEditText.error = "نام گروه نمی‌تواند خالی باشد"
            }
        }
    }

    private fun addGroup(groupName: String) {
        val request = AddGroupRequest(name = groupName)
        val call = RetrofitClient.getInstance(this).addGroup(request)

        call.enqueue(object : Callback<AddGroupResponse> {
            override fun onResponse(call: Call<AddGroupResponse>, response: Response<AddGroupResponse>) {
                if (response.isSuccessful) {
                    val addGroupResponse = response.body()
                    if (addGroupResponse != null) {
                        Toast.makeText(this@AddGroupActivity, "گروه با موفقیت اضافه شد", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AddGroupActivity, GroupDetailsActivity::class.java).apply {
                            putExtra("GROUP_ID", addGroupResponse.group.id)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@AddGroupActivity, "افزودن گروه ناموفق بود", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@AddGroupActivity, "Failed to add group", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddGroupResponse>, t: Throwable) {
                Toast.makeText(this@AddGroupActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
