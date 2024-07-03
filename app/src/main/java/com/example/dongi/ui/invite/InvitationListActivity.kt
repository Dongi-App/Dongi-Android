package com.example.dongi.ui.invite

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dongi.R
import com.example.dongi.api.Invitation
import com.example.dongi.api.InvitationListResponse
import com.example.dongi.api.InvitationResponseRequest
import com.example.dongi.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InvitationListActivity : AppCompatActivity() {

    private lateinit var invitationsRecyclerView: RecyclerView
    private lateinit var invitationAdapter: InvitationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invitation_list)

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

        invitationsRecyclerView = findViewById(R.id.invitationsRecyclerView)
        invitationsRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchInvitations()
    }

    private fun fetchInvitations() {
        val call = RetrofitClient.getInstance(this).getInvitations(true)

        call.enqueue(object : Callback<InvitationListResponse> {
            override fun onResponse(call: Call<InvitationListResponse>, response: Response<InvitationListResponse>) {
                if (response.isSuccessful) {
                    val invitations = response.body()?.invitations ?: emptyList()
                    invitationAdapter = InvitationAdapter(invitations, ::acceptInvitation, ::declineInvitation)
                    invitationsRecyclerView.adapter = invitationAdapter
                } else {
                    Toast.makeText(this@InvitationListActivity, "دریافت دعوت‌‌ها با خطا مواجه شد", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<InvitationListResponse>, t: Throwable) {
                Toast.makeText(this@InvitationListActivity, "مشکلی در ارتباط با سرور پیش آمد: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun acceptInvitation(invitation: Invitation) {
        respondToInvitation(invitation.id, true)
    }

    private fun declineInvitation(invitation: Invitation) {
        respondToInvitation(invitation.id, false)
    }

    private fun respondToInvitation(invitationId: String, admit: Boolean) {
        val request = InvitationResponseRequest(invitation_id = invitationId, admit = admit)
        val call = RetrofitClient.getInstance(this).respondToInvitation(request)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchInvitations() // Refresh the list
                } else {
                    Toast.makeText(this@InvitationListActivity, "دعوت‌نامه با خطا مواجه شد", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@InvitationListActivity, "مشکلی در ارتباط با سرور پیش آمد: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
