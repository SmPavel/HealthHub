package com.example.healthhub.Activity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.healthhub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class AboutUserActivity : AppCompatActivity() {
    private lateinit var btnBack: Button
    private lateinit var btnLogout: Button

    private lateinit var vtEmail: TextView
    private lateinit var etWeight: EditText
    private lateinit var etHeight: EditText

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_acc)

        auth = FirebaseAuth.getInstance()

        btnBack = findViewById(R.id.back)
        btnLogout = findViewById(R.id.logout)
        etWeight = findViewById(R.id.weight_num)
        etHeight = findViewById(R.id.height_num)
        vtEmail = findViewById(R.id.email_text)

        vtEmail.isSelected = true

        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val user = FirebaseAuth.getInstance().currentUser
        val userRef = FirebaseFirestore.getInstance().collection("userProfiles").document(user?.uid ?: "")
        userRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val email = document.getString("email")
                    val weightKg = document.getDouble("weightKg")
                    val heightCm = document.getDouble("heightCm")


                    vtEmail.text = email
                    etWeight.setText(weightKg?.toString())
                    etHeight.setText(heightCm?.toString())
                } else {
                    Log.d(TAG, "User document does not exist")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting user document: $exception")
            }

        etWeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val newWeight = etWeight.text.toString().toDoubleOrNull()
                if (newWeight != null) {
                    userRef.update("weightKg", newWeight)
                        .addOnSuccessListener {
                            Log.d(TAG, "Weight updated successfully")
                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "Error updating weight: $exception")
                        }
                }
            }
        })

        etHeight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val newHeight = etHeight.text.toString().toDoubleOrNull()
                if (newHeight != null) {
                    userRef.update("heightCm", newHeight)
                        .addOnSuccessListener {
                            Log.d(TAG, "Height updated successfully")
                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "Error updating height: $exception")
                        }
                }
            }
        })

    }
    }