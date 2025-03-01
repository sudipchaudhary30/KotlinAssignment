package com.example.sudiplearningnestquiz.activity.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sudiplearningnestquiz.databinding.ActivityLevelsBinding
import com.example.sudiplearningnestquiz.adapter.LevelAdoptor
import com.example.sudiplearningnestquiz.model.Levels
import com.example.sudiplearningnestquiz.model.PreviousLevels
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LevelsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLevelsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Levels"

        val rv: RecyclerView = binding.rv

        val listOfLevel = mutableListOf<Levels>()
        val dataOfAllLevel = mutableListOf<PreviousLevels>()

        val entertainment = listOf(
            "Anime", "Cartoon", "Books", "Film", "Music", "Television", "VideoGames", "BoardGames"
        )

        val categoryNo = listOf(
            31, 32, 10, 11, 12, 14, 15, 16
        )

        for (i in entertainment.indices) {
            listOfLevel.add(Levels(i + 1, entertainment[i], categoryNo[i]))
        }

        println(listOfLevel)

        val bundle: Bundle? = intent.extras
        val totalCorrectAns = bundle?.getInt("totalCorrectAns")
        val previousLevel = bundle?.getString("level")

        println("((((_____ActPLevel__(((${previousLevel}++++++++++++++++++++++++++++++")
        println("((((_____TotalC__(((${totalCorrectAns}++++++++++++++++++++++++++++++")

        var index = 0

        if (totalCorrectAns != null && previousLevel != null) {
            dataOfAllLevel.add(
                PreviousLevels(
                    totalCorrectAns,
                    previousLevel
                )
            )
            index = previousLevel.toInt() - 1
        } else {
            dataOfAllLevel.add(
                PreviousLevels(
                    -1,
                    "-1"
                )
            )
        }

        val db = Firebase.firestore

        val levelAdapter = LevelAdoptor(listOfLevel, this, dataOfAllLevel, index)
        rv.layoutManager = GridLayoutManager(this, 2)
        rv.adapter = levelAdapter
    }
}
