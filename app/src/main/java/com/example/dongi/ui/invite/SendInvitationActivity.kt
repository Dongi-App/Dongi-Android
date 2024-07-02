package com.example.dongi.ui.invite

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.dongi.R
import com.example.dongi.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendInvitationActivity : AppCompatActivity() {

    private lateinit var memberEmailEditText: EditText
    private lateinit var sendInvitationButton: Button
    private var groupId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_invitation)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        // Set the navigation icon color
        val navIcon = toolbar.navigationIcon
        if (navIcon != null) {
            val wrappedDrawable = DrawableCompat.wrap(navIcon)
            DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(this, R.color.secondaryButtonColor))
            toolbar.navigationIcon = wrappedDrawable
        }

        // Handle the navigation click to go back to the previous activity
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        memberEmailEditText = findViewById(R.id.memberEmailEditText)
        sendInvitationButton = findViewById(R.id.sendInvitationButton)

        // Get the GROUP_ID from the intent extras
        groupId = intent.getStringExtra("GROUP_ID")

        sendInvitationButton.setOnClickListener {
            sendInvitation()
        }
    }

    private fun sendInvitation() {
        val email = memberEmailEditText.text.toString().trim()
        if (email.isEmpty()) {
            Toast.makeText(this, "لطفا ایمیل را وارد کنید", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "ایمیل نامعتبر است", Toast.LENGTH_SHORT).show()
            return
        }

        val groupId = groupId ?: return
        val request = mapOf("user_email" to email, "group_id" to groupId)

        val call = RetrofitClient.getInstance(this).sendInvitation(request)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SendInvitationActivity, "دعوت‌نامه با موفقیت ارسال شد", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@SendInvitationActivity, "دعوت‌نامه ارسال نشد", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@SendInvitationActivity, "دعوت‌نامه ارسال نشد", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
