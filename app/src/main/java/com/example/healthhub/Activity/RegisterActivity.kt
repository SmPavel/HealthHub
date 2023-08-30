package com.example.healthhub.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPass: EditText
    private lateinit var etConfPass: EditText
    private lateinit var etWeight: EditText
    private lateinit var etHeight: EditText

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_screen)

        val btnSignUp = findViewById<Button>(R.id.signUp)
        val btnBack = findViewById<Button>(R.id.back)

        etUsername = findViewById(R.id.username)
        etPass = findViewById(R.id.password)
        etConfPass = findViewById(R.id.password_confirm)
        etWeight = findViewById(R.id.weight)
        etHeight = findViewById(R.id.height)


        auth = Firebase.auth

        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnSignUp.setOnClickListener {
            signUp()
        }
    }

        private fun signUp(){
            val username = etUsername.text.toString()
            val pass = etPass.text.toString()
            val confirmPassword = etConfPass.text.toString()
            val weight = etWeight.text.toString()
            val height = etHeight.text.toString()

            if (username.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
                Toast.makeText(this, "Username and Password can't be blank", Toast.LENGTH_SHORT).show()
                return
            }

            if (weight.isBlank() || height.isBlank()){
                Toast.makeText(this, "Please enter your weight and height", Toast.LENGTH_SHORT).show()
                return
            }

            if (pass != confirmPassword) {
                Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                    .show()
                return
            }

            auth.createUserWithEmailAndPassword(username, pass).addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Successfully Singed Up", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Singed Up Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }