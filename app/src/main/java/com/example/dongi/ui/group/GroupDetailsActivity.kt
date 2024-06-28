package com.example.dongi.ui.group

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.dongi.R

class GroupDetailsActivity : AppCompatActivity() {

    private lateinit var groupId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_details)

        groupId = intent.getStringExtra("GROUP_ID") ?: return

        Log.d("GroupDetailsActivity", "Group ID: $groupId")
    }
}
