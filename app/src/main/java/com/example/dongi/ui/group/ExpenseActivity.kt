package com.example.dongi.ui.group

import android.app.DatePickerDialog
import android.os.Bundle
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
import com.example.dongi.api.AddGroupResponse
import com.example.dongi.api.Expense
import com.example.dongi.api.RetrofitClient
import com.example.dongi.api.Share
import com.example.dongi.api.UserDataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class ExpenseActivity : AppCompatActivity() {

    private lateinit var groupId: String
    private lateinit var datePickerTV: TextView
    private lateinit var expenseReasonET: EditText
    private lateinit var expenseAmountET: EditText
    private lateinit var saveButton: Button
    private var members: List<Share> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

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

        datePickerTV = findViewById(R.id.date_picker)
        expenseReasonET = findViewById(R.id.expense_reason)
        expenseAmountET = findViewById(R.id.expense_amount)
        saveButton = findViewById(R.id.save_button)

        saveButton.setOnClickListener {
            if (validateInputs()) {
                getPayer()
                finish()
            }
        }
    }

    private fun addExpenseReq(addExp: AddExpenseRequest) {
        RetrofitClient.getInstance(this@ExpenseActivity).addExpense(addExp).enqueue(object : Callback<Expense> {
            override fun onResponse(call: Call<Expense>, response: Response<Expense>) {
                if (response.isSuccessful) {
                    val expenseSummary = response.body()
                    Toast.makeText(this@ExpenseActivity, "اضافه کردن خرج با موفقیت انجام شد.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ExpenseActivity, "اضافه کردن خرج مشکل مواجه شده است.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Expense>, t: Throwable) {
                Toast.makeText(this@ExpenseActivity, "اضافه کردن خرج با مشکل مواجه شده است.: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getPayer() {

        val reason = expenseReasonET.text.toString()
        val amount = expenseAmountET.text.toString()
        val date = datePickerTV.text.toString()

        RetrofitClient.getInstance(this).getUserData().enqueue(object : Callback<UserDataResponse> {
            override fun onResponse(call: Call<UserDataResponse>, response: Response<UserDataResponse>) {
                if (response.isSuccessful) {
                    val userData = response.body()?.user
                    if (userData != null) {
                        val addExp = AddExpenseRequest(group=groupId, payer=userData.email ,description=reason, amount=amount, date=date)
                        // add expense
                        addExpenseReq(addExp)
                    } else {
                        Toast.makeText(this@ExpenseActivity, "دریافت اطلاعات کاربر با مشکل مواجه شده است.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ExpenseActivity, "دریافت اطلاعات کاربر با مشکل مواجه شده است.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                Toast.makeText(this@ExpenseActivity, "دریافت اطلاعات کاربر با مشکل مواجه شده است.: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun validateInputs(): Boolean {
        val reason = expenseReasonET.text.toString().trim()
        val amount = expenseAmountET.text.toString().trim()
        val date = datePickerTV.text.toString().trim()

        if (reason.isEmpty()) {
            expenseReasonET.error = "اضافه کردن علت خرج الزامی است"
            expenseReasonET.requestFocus()
            return false
        }

        if (amount.isEmpty()) {
            expenseAmountET.error = "اضافه کردن مبلغ خرج الزامی است"
            expenseAmountET.requestFocus()
            return false
        }

        if (date.isEmpty()) {
            datePickerTV.error = "افزودن تاریخ خرج الزامی است"
            return false
        }

        return true
    }

    fun pickDate(view: android.view.View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedYear}-${selectedMonth}-${selectedDay}"
                datePickerTV.text = selectedDate
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}
