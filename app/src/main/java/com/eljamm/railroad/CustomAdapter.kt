package com.eljamm.railroad

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

/**
 * Custom ListView adapter that accepts an ArrayList of objects
 */
class CustomAdapter(context: Context, dataset: ArrayList<Station>) :
    ArrayAdapter<Station>(context, R.layout.item, dataset) {

    /**
     * Holds item Views for later modification
     */
    private inner class ViewHolder {
        lateinit var txtName:TextView
        lateinit var txtClass:TextView
        lateinit var imgType: ImageView
    }

    // Specify a custom type of View to use as a data object in the ListView
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val station: Station = getItem(position)!!  // Element at the given position of the dataset
        val viewHolder = ViewHolder()
        val mConvertView: View  // Should not be returned null or the app will crash

        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            mConvertView = layoutInflater.inflate(R.layout.item, parent, false)
        } else {
            // View already exists
            mConvertView = convertView
        }

        // Get item Views
        viewHolder.txtName = mConvertView.findViewById(R.id.txtName)
        viewHolder.txtClass = mConvertView.findViewById(R.id.txtType)
        viewHolder.imgType = mConvertView.findViewById(R.id.imgType)

        // If the variables were not initialized then we need to catch the exception
        try {
            // Modify item Views
            viewHolder.txtName.text = station.name
            viewHolder.txtClass.text = station.type

            // Set the items' icons according to type
            if (station.type == context.getString(R.string.station)) {
                viewHolder.imgType.setImageResource(R.drawable.ic_station)  // Set Station image
            } else if (station.type == context.getString(R.string.turnout)) {
                viewHolder.imgType.setImageResource(R.drawable.ic_turnout)  // Set Turnout image
            }
        } catch (e: UninitializedPropertyAccessException) {
            Log.d("TEST", "Late init variables not initialized.")
        }
        return mConvertView
    }
}