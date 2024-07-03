package com.example.dongi.ui.group

import android.content.Context
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
        val memberNameTextView: TextView = itemView.findViewById(R.id.memberNameTextView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        return MembersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MembersViewHolder, position: Int) {
        val user = users[position]
        holder.memberNameTextView.text = "${user.first_name} ${user.last_name}"
    }

    override fun getItemCount(): Int {
        return users.size
    }
}