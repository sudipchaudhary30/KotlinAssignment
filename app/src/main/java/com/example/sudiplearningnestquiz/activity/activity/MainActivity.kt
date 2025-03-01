package com.example.sudiplearningnestquiz.activity.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.example.sudiplearningnestquiz.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "QuizGame"

        // Firebase initialization
        firebaseAuth = FirebaseAuth.getInstance()

        // Start Quiz
        binding.startBtn.setOnClickListener {
            startActivity(Intent(this, LevelsActivity::class.java))
            Toast.makeText(this, "The Quiz has been started", Toast.LENGTH_SHORT).show()
        }

        // Example API response string containing encoded HTML entities
        val apiResponse = "Your API response containing &#039;"

        // Decode HTML entities
        val decodedText = HtmlCompat.fromHtml(apiResponse, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        println("**********($decodedText)************")

        // Logout
        binding.logOutBtn.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()  // Prevents navigating back to MainActivity after logout
        }
    }
}
