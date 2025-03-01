package com.example.sudiplearningnestquiz.activity.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sudiplearningnestquiz.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Sign In"
        firebaseAuth = FirebaseAuth.getInstance()

        // Login Button Click
        binding.loginBtn.setOnClickListener {
            val email = binding.emailET.text.toString().trim()
            val pass = binding.passET.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "User logged in successfully", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, task.exception?.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                when {
                    email.isEmpty() && pass.isEmpty() -> {
                        Toast.makeText(this, "Please enter Email & Password", Toast.LENGTH_SHORT).show()
                    }
                    email.isEmpty() -> {
                        Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show()
                    }
                    pass.isEmpty() -> {
                        Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Go to Sign Up activity
        binding.gotoSignUpTV.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            Toast.makeText(this, "Signed in as ${currentUser.email}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
