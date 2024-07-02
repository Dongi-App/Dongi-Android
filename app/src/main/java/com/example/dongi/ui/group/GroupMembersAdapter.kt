package com.example.dongi.ui.group

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dongi.R
import com.example.dongi.api.User

class GroupMembersAdapter(private val context: Context, private val users: List<User>) : RecyclerView.Adapter<GroupMembersAdapter.MembersViewHolder>() {

    inner class MembersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val groupNameTextView: TextView = itemView.findViewById(R.id.groupNameTextView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val user = users[position]
                val intent = Intent(context, GroupDetailsActivity::class.java).apply {
                    putExtra("USER_EMAIL", user.email)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return MembersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MembersViewHolder, position: Int) {
        val group = users[position]
        holder.groupNameTextView.text = group.first_name
    }

    override fun getItemCount(): Int {
        return users.size
    }
}