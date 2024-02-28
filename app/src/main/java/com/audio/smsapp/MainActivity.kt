package com.audio.smsapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var editTextPhone: EditText
    private lateinit var editTextMessage: EditText

    private val REQUEST_SMS_PERMISSIONS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextPhone = findViewById(R.id.editTextPhone)
        editTextMessage = findViewById(R.id.editTextMessage)

        val buttonSend: Button = findViewById(R.id.buttonSend)
        buttonSend.setOnClickListener {
            requestSmsPermissions()
        }
    }

    private fun requestSmsPermissions() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        )

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.SEND_SMS),
                REQUEST_SMS_PERMISSIONS
            )
        } else {
            sendMessage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_SMS_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendMessage()
            } else {
                showPermissionDeniedSnackbar()
            }
        }
    }

    private fun sendMessage() {
        val phoneNum = editTextPhone.text.toString()
        val message = editTextMessage.text.toString()

        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNum, null, message, null, null)
    }

    private fun showPermissionDeniedSnackbar() {
        val rootView: View = findViewById(android.R.id.content)
        Snackbar.make(rootView, "You can't send SMS without permission", Snackbar.LENGTH_LONG).show()
    }
}