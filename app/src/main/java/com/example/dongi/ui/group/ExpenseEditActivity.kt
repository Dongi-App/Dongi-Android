package com.example.dongi.ui.group

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.dongi.R
import com.example.dongi.api.ExpenseData
import com.example.dongi.api.RemoveExpenseRequest
import com.example.dongi.api.RetrofitClient
import com.example.dongi.api.Share
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExpenseEditActivity : AppCompatActivity() {

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

        RetrofitClient.getInstance(this@ExpenseEditActivity).getExpenseDetails(expenseId).enqueue(object : Callback<ExpenseData> {
            override fun onResponse(call: Call<ExpenseData>, response: Response<ExpenseData>) {
                if (response.isSuccessful) {
                    val expenseResponse = response.body()
                    if (expenseResponse != null) {
                        expenseReasonET.text = expenseResponse.expense.description
                        expenseAmountET.text = "${expenseResponse.expense.amount / expenseResponse.expense.total_shares}"
                        datePickerTV.text = expenseResponse.expense.date
                        collectorTV.text = expenseResponse.expense.payer
                    } else {
                        Toast.makeText(this@ExpenseEditActivity, "دریافت اطلاعات خرج با مشکل مواجه شد", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ExpenseEditActivity, "Failed to load expense details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ExpenseData>, t: Throwable) {
                Log.e("API Error", "Error: ${t.message}")
                Toast.makeText(this@ExpenseEditActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun settleExpense(view: View) {
        val removeExpReq = RemoveExpenseRequest(expenseId)
        RetrofitClient.getInstance(this@ExpenseEditActivity).removeExpense(removeExpReq).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    finish()
                } else {
                    Toast.makeText(this@ExpenseEditActivity, "Failed to remove expense details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API Error", "Error: ${t.message}")
                Toast.makeText(this@ExpenseEditActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
