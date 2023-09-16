package com.example.healthhub.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.healthhub.R

// Define the data class representing the data from Firestore
data class MyData(val foodName: String, val calorie: Long, val proteins: Long, val fats: Long, val carbohydrates: Long)

// Create the adapter class, inherited from the base ArrayAdapter class for ListView
class CalorieTableListViewAdapter(
    private val context: Context,
    private val data: List<MyData>
) : ArrayAdapter<MyData>(context, R.layout.custom_calorie_table_row, data) {

    // The getView method is called for each ListView row and returns the View for its display
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.custom_calorie_table_row, parent, false)
        }
        val item = data[position]

        val foodName = view!!.findViewById<TextView>(R.id.text_food_name)
        val calorie = view.findViewById<TextView>(R.id.text_calorie_per_100g)
        val proteins = view.findViewById<TextView>(R.id.text_proteins)
        val fats = view.findViewById<TextView>(R.id.text_fats)
        val carbohydrates = view.findViewById<TextView>(R.id.text_carbohydrates)

        foodName.text = item.foodName
        calorie.text = item.calorie.toString()
        proteins.text = item.proteins.toString()
        fats.text = item.fats.toString()
        carbohydrates.text = item.carbohydrates.toString()

        return view
    }
}
