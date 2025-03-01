package com.example.sudiplearningnestquiz.activity.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sudiplearningnestquiz.databinding.ActivityFinishBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FinishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinishBinding
    private var dataOfAllLevel = mutableListOf<PreviousLevels>()
    private var i = 0
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        val bundle: Bundle? = intent.extras
        val totalCorrectAns = bundle?.getInt("correctAnswer") ?: 0
        val totalQuestion = bundle?.getInt("questionSize") ?: 0
        val level = bundle?.getInt("level") ?: -1

        Log.i("FinishActivity", "Level: $level, Correct Answers: $totalCorrectAns")

        binding.score.text = "$totalCorrectAns / $totalQuestion"

        // Setting star images based on score
        setStars(totalCorrectAns)

        if (level != -1) {
            storeData(totalCorrectAns, level)
        } else {
            storeData(-1, -1)
        }

        binding.quitBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.nextBtn.setOnClickListener {
            val intent = Intent(this, LevelsActivity::class.java)
            intent.putExtra("totalCorrectAns", totalCorrectAns)
            intent.putExtra("level", level.toString())
            startActivity(intent)
            finish()
        }

        binding.restartBtn.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("level", level)
            startActivity(intent)
            finish()
        }
    }

    private fun setStars(correctAnswers: Int) {
        binding.star1IV.setImageResource(R.drawable.star_2956792)
        binding.star2IV.setImageResource(R.drawable.star_2956792)
        binding.star3IV.setImageResource(R.drawable.star_2956792)

        if (correctAnswers >= 1) binding.star1IV.setImageResource(R.drawable.star_14441715)
        if (correctAnswers >= 2) binding.star2IV.setImageResource(R.drawable.star_14441715)
        if (correctAnswers >= 3) binding.star3IV.setImageResource(R.drawable.star_14441715)
    }

    private fun storeData(totalCorrectAns: Int, previousLevel: Int) {
        val userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            Log.w("FinishActivity", "User ID is null. Data storage skipped.")
            return
        }

        val userRef = db.collection("users").document(userId)
        val userData = hashMapOf(
            "levelNo" to previousLevel,
            "correctAns" to totalCorrectAns.toString()
        )

        userRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val existingData = documentSnapshot.data?.toMutableMap() ?: mutableMapOf()
                existingData[i.toString()] = userData
                userRef.update(existingData)
                    .addOnSuccessListener { Log.d("FinishActivity", "Document updated successfully") }
                    .addOnFailureListener { e -> Log.w("FinishActivity", "Error updating document", e) }
            } else {
                userRef.set(userData)
                    .addOnSuccessListener { Log.d("FinishActivity", "New user document created.") }
                    .addOnFailureListener { e -> Log.w("FinishActivity", "Error creating document", e) }
            }
        }.addOnFailureListener { e ->
            Log.w("FinishActivity", "Error fetching document", e)
        }
    }
}
