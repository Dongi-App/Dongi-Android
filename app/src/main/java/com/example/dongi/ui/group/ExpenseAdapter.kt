package com.example.dongi.ui.group

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dongi.R
import com.example.dongi.api.Expense

class ExpenseAdapter(private val context: Context, private val expenses: List<Expense>) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val expenseInfoTextView: TextView = itemView.findViewById(R.id.expenseInfoTextView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val expense = expenses[position]
                val intent = Intent(context, ExpenseEditActivity::class.java).apply {
                    putExtra("EXPENSE_ID", expense.id)
                    putExtra("GROUP_ID", expense.group)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.expenseInfoTextView.text = "${expense.description} (${expense.date})"
    }

    override fun getItemCount(): Int {
        return expenses.size
    }
}