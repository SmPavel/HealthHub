package com.example.healthhub.Activity

import WorkoutData
import WorkoutPlanAdapter
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthhub.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore


class WorkoutActivity : AppCompatActivity() {
    private lateinit var etWorkName: EditText
    private lateinit var etWorkCount: EditText

    private lateinit var btnAdd: AppCompatImageButton
    private lateinit var btnDelete: FloatingActionButton
    private lateinit var btnBack: Button

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: WorkoutPlanAdapter
    private lateinit var db: FirebaseFirestore
    private val data = mutableListOf<WorkoutData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.your_workout_plan)

        etWorkName = findViewById(R.id.exercises_name)
        etWorkCount = findViewById(R.id.exercises_count)

        btnAdd = findViewById(R.id.add_button)
        btnDelete = findViewById(R.id.delete_button)
        btnBack = findViewById(R.id.back)

        recyclerView = findViewById(R.id.exercisesView)

        db = FirebaseFirestore.getInstance()
        adapter = WorkoutPlanAdapter(this, data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchData()

        btnAdd.setOnClickListener {
            // Получение текста из EditText
            val workName = etWorkName.text.toString().trim()
            val workCountText = etWorkCount.text.toString().trim()

            // Проверка, что значения не являются пустыми строками
            if (workName.isNotEmpty() && workCountText.isNotEmpty()) {
                val workCount = workCountText.toLong()
                val competed = false

                // Создание нового документа в коллекции "workout"
                db.collection("workout")
                    .add(
                        mapOf(
                            "name" to workName,
                            "count" to workCount,
                            "completed" to competed
                        )
                    )
                    .addOnSuccessListener { documentReference ->
                        fetchData()

                        // Очистка содержимого EditText
                        etWorkName.setText("")
                        etWorkCount.setText("")
                    }
            }
        }


        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

              btnDelete.setOnClickListener{
                  deleteCompletedExercises()
            }
    }
    private fun fetchData() {
        db.collection("workout")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val snapshotList = querySnapshot.documents
                data.clear()
                for (document in snapshotList) {
                    if (document != null && document.exists()) {
                        val workData = WorkoutData(
                            document["name"] as String,
                            document["count"] as Long,
                            document["completed"] as? Boolean ?: false,
                            document.id
                        )
                        data.add(workData)
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun deleteCompletedExercises() {
        db.collection("workout")
            .whereEqualTo("completed", true)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    db.collection("workout").document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "Document successfully deleted!")
                            data.clear() // Очищаем список data
                            fetchData() // Получаем обновленные данные из Firebase Firestore
                            adapter.notifyDataSetChanged() // Обновляем отображение в RecyclerView
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error deleting document", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error retrieving documents", e)
            }
    }
}