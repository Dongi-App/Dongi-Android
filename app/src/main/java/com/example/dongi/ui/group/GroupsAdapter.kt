package com.example.dongi.ui.group

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dongi.R
import com.example.dongi.api.Group

class GroupsAdapter(private val context: Context, private val groups: List<Group>) : RecyclerView.Adapter<GroupsAdapter.GroupViewHolder>() {

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val groupNameTextView: TextView = itemView.findViewById(R.id.groupNameTextView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val group = groups[position]
                val intent = Intent(context, GroupDetailsActivity::class.java).apply {
                    putExtra("GROUP_ID", group.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.groupNameTextView.text = group.name
    }

    override fun getItemCount(): Int {
        return groups.size
    }
}