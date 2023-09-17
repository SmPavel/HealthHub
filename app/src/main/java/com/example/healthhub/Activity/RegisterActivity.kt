package com.example.healthhub.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.healthhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var btnSignUp: Button
    private lateinit var btnBack: Button

    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    private lateinit var etConfPass: EditText
    private lateinit var etWeight: EditText
    private lateinit var etHeight: EditText

    private val TAG = "RegisterActivity"

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_screen)

        btnSignUp = findViewById(R.id.signUp)
        btnBack = findViewById(R.id.back)

        etEmail = findViewById(R.id.email)
        etPass = findViewById(R.id.password)
        etConfPass = findViewById(R.id.password_confirm)
        etWeight = findViewById(R.id.weight)
        etHeight = findViewById(R.id.height)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnSignUp.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()
        val confirmPassword = etConfPass.text.toString()
        val weight = etWeight.text.toString()
        val height = etHeight.text.toString()

        if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Username and Password can't be blank", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (weight.isBlank() || height.isBlank()) {
            Toast.makeText(this, "Please enter your weight and height", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (pass != confirmPassword) {
            Toast.makeText(
                this,
                "Password and Confirm Password do not match",
                Toast.LENGTH_SHORT
            )
                .show()
            return
        }
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        val userRef = FirebaseFirestore.getInstance().collection("userProfiles").document(user.uid)

                        val userData = hashMapOf(
                            "email" to email,
                            "weightKg" to weight.toDouble(),
                            "heightCm" to height.toDouble()
                        )

                        userRef.set(userData)
                            .addOnSuccessListener {
                                Log.d(TAG, "User profile created successfully")
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener { error ->
                                Log.w(TAG, "Error creating user profile:", error)
                                Toast.makeText(
                                    this, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}