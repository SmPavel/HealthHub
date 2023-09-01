package com.example.healthhub.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.healthhub.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        val btnCalorie = findViewById<ImageButton>(R.id.calorie_table_button)
        val btnWorkout = findViewById<ImageButton>(R.id.your_exercises_button)
        val btnMenu = findViewById<ImageButton>(R.id.menu_button)

        btnCalorie.setOnClickListener {
            val intent = Intent(this, CalorieTableActivity::class.java)
            startActivity(intent)
        }

        btnWorkout.setOnClickListener {
            val intent = Intent(this, WorkoutActivity::class.java)
            startActivity(intent)
        }
        btnMenu.setOnClickListener {
            val intent = Intent(this, DrawerActivity::class.java)
            startActivity(intent)
        }
    }
}