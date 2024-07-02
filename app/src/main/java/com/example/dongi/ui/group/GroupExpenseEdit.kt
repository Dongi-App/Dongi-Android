package com.example.dongi.ui.group

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.dongi.R
import com.example.dongi.api.AddExpenseRequest
import com.example.dongi.api.ExpenseData
import com.example.dongi.api.Group
import com.example.dongi.api.RetrofitClient
import com.example.dongi.api.Share
import com.example.dongi.api.UserDataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class GroupExpenseEdit : AppCompatActivity() {

    private lateinit var groupId: String
    private lateinit var expenseId: String
    private lateinit var datePickerTV: TextView
    private lateinit var expenseReasonET: TextView
    private lateinit var collectorTV: TextView
    private lateinit var expenseAmountET: TextView
    private lateinit var removeButton: Button
    private lateinit var members: List<Share>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

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

        groupId = intent.getStringExtra("GROUP_ID") ?: return
        expenseId = intent.getStringExtra("EXPENSE_ID") ?: return


        datePickerTV = findViewById(R.id.date_picker)
        expenseReasonET = findViewById(R.id.expense_reason)
        collectorTV = findViewById(R.id.rec_text_view)
        expenseAmountET = findViewById(R.id.expense_amount)
        removeButton = findViewById(R.id.remove_button)

        fetchExpenseData()

    }

    private fun fetchExpenseData() {

        RetrofitClient.getInstance(this@GroupExpenseEdit).getExpenseDetails(expenseId).enqueue(object : Callback<ExpenseData> {
            override fun onResponse(call: Call<ExpenseData>, response: Response<ExpenseData>) {
                if (response.isSuccessful) {
                    val expenseResponse = response.body()
                    if (expenseResponse != null) {
                        expenseReasonET.text = expenseResponse.expense.description
                        expenseAmountET.text = "${expenseResponse.expense.amount / expenseResponse.expense.total_shares}"
                        datePickerTV.text = expenseResponse.expense.date
                        collectorTV.text = expenseResponse.expense.payer
                    } else {
                        Toast.makeText(this@GroupExpenseEdit, "دریافت اطلاعات خرج با مشکل مواجه شد", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@GroupExpenseEdit, "Failed to load expense details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ExpenseData>, t: Throwable) {
                Log.e("API Error", "Error: ${t.message}")
                Toast.makeText(this@GroupExpenseEdit, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun settleExpense(view: View) {
        RetrofitClient.getInstance(this@GroupExpenseEdit).removeExpense(expenseId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val intent = Intent(this@GroupExpenseEdit, GroupDetailsActivity::class.java).apply {
                        putExtra("GROUP_ID", groupId)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this@GroupExpenseEdit, "Failed to remove expense details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API Error", "Error: ${t.message}")
                Toast.makeText(this@GroupExpenseEdit, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
