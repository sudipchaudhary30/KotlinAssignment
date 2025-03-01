package com.example.sudiplearningnestquiz.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sudiplearningnestquiz.databinding.ActivitySignUpBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Sign Up"

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        // Sign-up button click listener
        binding.signUpBtn.setOnClickListener {
            val email = binding.emailET.text.toString().trim()
            val pass = binding.passET.text.toString().trim()
            val confirmPass = binding.confirmPassET.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass != confirmPass) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Navigate to login page
        binding.gotoLoginTV.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
