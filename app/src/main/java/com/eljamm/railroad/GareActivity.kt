package com.eljamm.railroad

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

private const val REQUEST_PERMISSION = 1
private const val STATION_NUMBER = "00000000" // (Placeholder)

class GareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gare)

        val imgGare = findViewById<ImageView>(R.id.imgGare)
        val tName = findViewById<TextView>(R.id.tName)

        val stationName = findViewById<TextView>(R.id.txtGareName)
        val stationAddress = findViewById<TextView>(R.id.txtGareAddress)

        val extras = intent.extras
        if (extras != null) {
            // Get station information
            val type = extras.getString("type")
            val name = extras.getString("name")
            val address = extras.getString("address")

            // Set the image's icon according to type
            if (type == getString(R.string.station)) {
                imgGare.setImageResource(R.drawable.ic_station)
            } else if (type == getString(R.string.turnout)) {
                imgGare.setImageResource(R.drawable.ic_turnout)
            }

            // Set the textViews' values
            tName.text = getString(R.string.name, type)
            stationName.text = name
            stationAddress.text = address
        }
    }

    // Option menu on the top right corner
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.apply {
            // Closes the activity
            add("Return").setOnMenuItemClickListener {
                finish()
                true
            }

            // Calls the Station number
            add("Call Station").setOnMenuItemClickListener {
                checkCallPermission()
                try {
                    callNumber(STATION_NUMBER)
                    Log.d("TEST", "Phone call successful")
                } catch (e: SecurityException) {
                    Log.d("TEST", "Permission not granted to make phone calls")
                }
                true
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    // Make a phone call
    private fun callNumber(number: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel: $number"))
        startActivity(intent)
    }

    /*
    Check if the user has enabled the call permission or not.

    Starting in SDK 30, if the user denies the permission twice then he has to enable
    it manually from the settings:
    https://developer.android.com/training/permissions/requesting#handle-denial
     */
    private fun checkCallPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If user has already denied the permission once
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                // Explain to user why the permission is necessary
                AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("Permission is needed to make phone calls.")
                    .setPositiveButton("OK") { _, _ ->      // Directly call OnClickListener
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.CALL_PHONE),
                            REQUEST_PERMISSION
                        )
                    }
                    .setNegativeButton("Cancel") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }.create().show()
            } else {
                // First time checking permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PERMISSION)
            }
        }
        return
    }

    // Indicate to the user that the permissions were granted successfully
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thank you for enabling the permission",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}