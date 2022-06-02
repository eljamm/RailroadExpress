package com.eljamm.railroad

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.PopupMenu

private const val STATION_WEBSITE = "https://duckduckgo.com/" // (Placeholder)

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private val stationList = ArrayList<Station>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.lst)

        // Fill the station list with information
        stationList.add(Station("Bryxton", "Station", "North Bryxton"))
        stationList.add(Station("Bryxton", "Turnout", "Central Bryxton"))
        stationList.add(Station("Costsworth", "Station", "North Costsworth"))
        stationList.add(Station("Cruxview", "Station", "East Cruxview"))
        stationList.add(Station("Cruxview", "Turnout", "Central Cruxview"))
        stationList.add(Station("Foxbrough", "Station", "South Foxbrough"))
        stationList.add(Station("Mayfield", "Station", "Central Mayfield"))
        stationList.add(Station("Mayfield", "Turnout", "West Mayfield"))
        stationList.add(Station("Starrville", "Station", "East Starrville"))
        stationList.add(Station("Starrville", "Station", "South Starrville"))
        stationList.add(Station("Tryxon", "Station", "Central Tryxon"))
        stationList.add(Station("Tryxon", "Turnout", "West Tryxon"))

        listView.adapter = CustomAdapter(this, stationList)

        // When the user clicks on an item go to GareActivity
        listView.setOnItemClickListener { _, _, i, _ ->
            gotoGare(applicationContext, i)
        }

        // When the user long clicks an item show the overflow menu
        listView.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { _, view, i, _ ->
                showOverflowMenu(this, view, i)
            true
        }
    }

    private fun showOverflowMenu(context: Context, anchor: View, i: Int) {
        val menu = PopupMenu(context, anchor)

        // Choices for the menu items
        menu.menu.apply {
            // Visit Website
            add(getString(R.string.visit_website)).setOnMenuItemClickListener {
                openWebsite(STATION_WEBSITE)
                true
            }
            // View Details
            add(getString(R.string.view_details)).setOnMenuItemClickListener {
                gotoGare(context, i)
                true
            }
        }

        menu.show()
    }

    // Goes to activity GareActivity
    private fun gotoGare(context: Context, position: Int) {
        val intent = Intent(context, GareActivity::class.java)

        // Put station information
        intent.putExtra("name", stationList[position].name)
        intent.putExtra("type", stationList[position].type)
        intent.putExtra("address", stationList[position].address)

        startActivity(intent)
    }

    // Opens a domain in the browser
    private fun openWebsite(domain: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(domain))
        startActivity(intent)
    }
}