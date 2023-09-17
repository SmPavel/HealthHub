package com.example.healthhub.Activity

import com.example.healthhub.Adapter.CalorieTableListViewAdapter
import com.example.healthhub.Adapter.MyData
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.healthhub.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldPath

class CalorieTableActivity : AppCompatActivity() {
    private lateinit var btnBack: Button
    private lateinit var searchBar: SearchView
    private lateinit var listView: ListView
    private lateinit var adapter: CalorieTableListViewAdapter
    private lateinit var db: FirebaseFirestore
    private val originalData = mutableListOf<MyData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calorie_table)

        btnBack = findViewById(R.id.back)
        searchBar = findViewById(R.id.search_bar)
        listView = findViewById(R.id.listview)

        db = FirebaseFirestore.getInstance()
        adapter = CalorieTableListViewAdapter(this, originalData)
        listView.adapter = adapter

        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { performSearch(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    originalData.clear()
                    adapter.notifyDataSetChanged()
                    fetchInitialData()
                } else {
                    newText?.let { performSearch(it) }
                }
                return false
            }
        })

        fetchInitialData()
    }

    private fun fetchInitialData() {
        db.collection("calorieTable")
            .limit(5)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val snapshotList = querySnapshot.documents
                for (document in snapshotList) {
                    if (document != null && document.exists()) {
                        val myData = MyData(
                            document["name"] as String,
                            document[FieldPath.of("calorie/100g")] as Long,
                            document["proteins"] as Long,
                            document["fats"] as Long,
                            document["carbohydrates"] as Long
                        )
                        originalData.add(myData)
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun performSearch(query: String) {
        originalData.clear()
        adapter.notifyDataSetChanged()

        db.collection("calorieTable")
            .orderBy("name")
            .startAt(query)
            .endAt(query + "\uf8ff")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val snapshotList = querySnapshot.documents
                for (document in snapshotList) {
                    if (document != null && document.exists

                            ()) {
                        val myData = MyData(
                            document["name"] as String,
                            document[FieldPath.of("calorie/100g")] as Long,
                            document["proteins"] as Long,
                            document["fats"] as Long,
                            document["carbohydrates"] as Long
                        )
                        originalData.add(myData)
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }
}