package com.example.dongi.ui.group

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.dongi.api.AddExpenseRequest
import androidx.appcompat.app.AppCompatActivity
import com.example.dongi.R
import com.example.dongi.api.RetrofitClient
import com.example.dongi.api.UserDataResponse
import com.example.dongi.api.Group
import com.example.dongi.api.Share
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class GroupDetailsActivity : AppCompatActivity() {

    private lateinit var groupId: String
    private lateinit var datePickerTV: TextView
    private lateinit var expenseReasonET: EditText
    private lateinit var expenseAmountET: EditText
    private lateinit var saveButton: Button
    private lateinit var members: Array<Share>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_details)

        groupId = intent.getStringExtra("GROUP_ID") ?: return
        Log.d("GroupDetailsActivity", "Group ID: $groupId")

        datePickerTV = findViewById(R.id.date_picker)
        expenseReasonET = findViewById(R.id.expense_reason)
        expenseAmountET = findViewById(R.id.expense_amount)
        saveButton = findViewById(R.id.save_button)

        saveButton.setOnClickListener {
            if (validateInputs()) {
                addExpense()
            }
        }
    }

    private fun getGroupMembers() {
        val groupInfo = RetrofitClient.getInstance(this).getGroupDetails(groupId)


        groupInfo.enqueue(object : Callback<Group> {
            override fun onResponse(call: Call<Group>, response: Response<Group>) {
                if (response.isSuccessful) {
                    val groupData = response.body()
                    if (groupData != null) {
                        val groupMembers = groupData.members
                        if (groupMembers.isNotEmpty()) {
                            val shareVal = 1.0/ groupMembers.size
                            val mutableList = members.toMutableList()
                            for (gm in groupMembers) {
                                mutableList.add(Share(gm.email, shareVal.toString()))
                            }
                            members = mutableList.toTypedArray()
                        }
                    } else {
                        Toast.makeText(this@GroupDetailsActivity, "دریافت اطلاعات گروه با مشکل مواجه شده است.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@GroupDetailsActivity, "دریافت اطلاعات کروه با مشکل مواجه شده است.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Group>, t: Throwable) {
                Toast.makeText(this@GroupDetailsActivity, "دریافت اطلاعات گروه با مشکل مواجه شده است.: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addExpense() {
        val reason = expenseReasonET.text.toString().trim()
        val amount = expenseAmountET.text.toString().trim()
        val date = datePickerTV.text.toString().trim()
        val call = RetrofitClient.getInstance(this).getUserData()

        call.enqueue(object : Callback<UserDataResponse> {
            override fun onResponse(call: Call<UserDataResponse>, response: Response<UserDataResponse>) {
                if (response.isSuccessful) {
                    val userData = response.body()?.user
                    if (userData != null) {
                        val addExp = AddExpenseRequest(group=groupId, payer=userData.email ,description=reason, amount=amount, date=date, shares=members)
                    } else {
                        Toast.makeText(this@GroupDetailsActivity, "دریافت اطلاعات کاربر با مشکل مواجه شده است.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@GroupDetailsActivity, "دریافت اطلاعات کاربر با مشکل مواجه شده است.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                Toast.makeText(this@GroupDetailsActivity, "دریافت اطلاعات کاربر با مشکل مواجه شده است.: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun validateInputs(): Boolean {
        val reason = expenseReasonET.text.toString().trim()
        val amount = expenseAmountET.text.toString().trim()
        val date = datePickerTV.text.toString().trim()

        if (reason.isEmpty()) {
            expenseReasonET.error = "اضافه کردن علت الزامی است"
            expenseReasonET.requestFocus()
            return false
        }

        if (amount.isEmpty()) {
            expenseAmountET.error = "اضافه کردن مبلغ الزامی است"
            expenseAmountET.requestFocus()
            return false
        }

        if (date.isEmpty()) {
            datePickerTV.error = "افزودن تاریخ الزامی است"
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
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                datePickerTV.text = selectedDate
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}
