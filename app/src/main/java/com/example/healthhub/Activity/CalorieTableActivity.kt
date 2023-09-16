package com.example.healthhub.Activity

import com.example.healthhub.Adapter.CalorieTableListViewAdapter
import com.example.healthhub.Adapter.MyData
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.healthhub.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.FieldPath;


class CalorieTableActivity : AppCompatActivity() {
    private lateinit var btnBack: Button

    private lateinit var searchBar: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calorie_table)

        btnBack = findViewById(R.id.back)
        searchBar = findViewById(R.id.search_bar)

        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val db = FirebaseFirestore.getInstance()
                val query: Query = db.collection("calorieTable")
                    .whereEqualTo("foodName", query)
                query.get().addOnSuccessListener(OnSuccessListener<QuerySnapshot> { querySnapshot ->
                    val snapshotList = querySnapshot.documents
                })

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val db = FirebaseFirestore.getInstance()
                val query = db.collection("calorieTable")
                    .orderBy("foodName").startAt(newText)
                    .endAt(newText + "\uf8ff")
                query.get().addOnSuccessListener { querySnapshot ->
                    val snapshotList = querySnapshot.documents
                    // Handle filtered results
                }
                return false
            }
        })
        // Get the data from Firestore and associate it with ListView through the adapter
        val listView = findViewById<ListView>(R.id.listview)
        val db = FirebaseFirestore.getInstance()

        db.collection("calorieTable")
            .limit(5)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val data = querySnapshot.documents.mapNotNull { document ->
                    if (document != null && document.exists()) {
                        MyData(
                            document["name"] as String,
                            document[FieldPath.of("calorie/100g")] as Long,
                            document["proteins"] as Long,
                            document["fats"] as Long,
                            document["carbohydrates"] as Long
                        )
                    } else {
                        null
                    }
                }

                val adapter = CalorieTableListViewAdapter(this, data)
                listView.adapter = adapter
            }
    }
}