package com.example.dongi.ui.invite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dongi.R
import com.example.dongi.api.Invitation

class InvitationAdapter(
    private val invitations: List<Invitation>,
    private val onAcceptClick: (Invitation) -> Unit,
    private val onDeclineClick: (Invitation) -> Unit
) : RecyclerView.Adapter<InvitationAdapter.InvitationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_invitation, parent, false)
        return InvitationViewHolder(view)
    }

    override fun onBindViewHolder(holder: InvitationViewHolder, position: Int) {
        val invitation = invitations[position]
        holder.bind(invitation)
    }

    override fun getItemCount(): Int {
        return invitations.size
    }

    inner class InvitationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val groupNameTextView: TextView = itemView.findViewById(R.id.groupNameTextView)
        private val acceptImageView: ImageView = itemView.findViewById(R.id.acceptImageView)
        private val declineImageView: ImageView = itemView.findViewById(R.id.declineImageView)

        fun bind(invitation: Invitation) {
            groupNameTextView.text = invitation.group.name
            acceptImageView.setOnClickListener { onAcceptClick(invitation) }
            declineImageView.setOnClickListener { onDeclineClick(invitation) }
        }
    }
}
