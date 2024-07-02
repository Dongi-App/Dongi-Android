package com.example.dongi.ui.group

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dongi.R
import com.example.dongi.api.ExpenseList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.dongi.api.RetrofitClient

class ExpenseListActivity : AppCompatActivity() {

    private lateinit var expensesRecyclerView: RecyclerView
    private lateinit var expensesAdapter: ExpenseAdapter
    private lateinit var groupId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses_list)
        groupId = intent.getStringExtra("GROUP_ID") ?: return


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

        expensesRecyclerView = findViewById(R.id.expensesRecyclerView)
        expensesRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        fetchGroups()
    }

    private fun fetchGroups() {
        RetrofitClient.getInstance(this).getExpenseList(groupId).enqueue(object : Callback<ExpenseList> {
            override fun onResponse(call: Call<ExpenseList>, response: Response<ExpenseList>) {
                if (response.isSuccessful) {
                    val expensesResponse = response.body()
                    if (expensesResponse != null) {
                        expensesAdapter = ExpenseAdapter(this@ExpenseListActivity, expensesResponse.expenses)
                        expensesRecyclerView.adapter = expensesAdapter
                    } else {
                        Toast.makeText(this@ExpenseListActivity, "دریافت گروه‌ها با مشکل مواجه شد", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ExpenseListActivity, "Failed to load expenses", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ExpenseList>, t: Throwable) {
                Log.e("API Error", "Error: ${t.message}")
                Toast.makeText(this@ExpenseListActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}