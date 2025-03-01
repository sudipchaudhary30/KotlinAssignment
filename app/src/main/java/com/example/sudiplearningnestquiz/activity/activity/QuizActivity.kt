package com.example.sudiplearningnestquiz.activity.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.example.sudiplearningnestquiz.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private lateinit var questionTV: TextView
    private lateinit var option1Btn: Button
    private lateinit var option2Btn: Button
    private lateinit var option3Btn: Button
    private lateinit var option4Btn: Button
    private lateinit var nextBtn: Button
    private lateinit var repo: Repo
    private lateinit var viewModel: ViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    private var index = 0
    private var totalCorrectAnswer = 0
    private val questionSize = 3
    private var categoryNo = 32
    private var level: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        questionTV = binding.questionTV
        option1Btn = binding.option1Btn
        option2Btn = binding.option2Btn
        option3Btn = binding.option3Btn
        option4Btn = binding.option4Btn
        nextBtn = binding.nextBtn

        // Retrieve level and category number
        level = intent.getIntExtra("level", 1)
        val list = intent.getParcelableArrayListExtra<Levels>("list")

        categoryNo = list?.getOrNull(level - 1)?.categoryNo ?: 32
        supportActionBar?.title = list?.getOrNull(level - 1)?.category ?: "QuizGame"

        fetchDataMVVM()

        nextBtn.setOnClickListener {
            if (index < questionSize) {
                resetOptionBackground()
                fetchDataMVVM()
            } else {
                val intent = Intent(this, FinishActivity::class.java)
                intent.putExtra("correctAnswer", totalCorrectAnswer)
                intent.putExtra("questionSize", questionSize)
                intent.putExtra("level", level)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun fetchDataMVVM() {
        viewModel.getQuiz(questionSize, categoryNo, "easy")
        viewModel.quizLiveData.observe(this) { responseBody ->
            if (responseBody != null) {
                if (index < questionSize) {
                    chal(responseBody)
                    index++
                }
            } else {
                Toast.makeText(this, "Failed to load quiz data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun chal(responseBody: QuizStructure) {
        if (index >= questionSize) return

        val result = responseBody.results.getOrNull(index) ?: return

        if (result.incorrect_answers.size < 3 || responseBody.response_code == 5) {
            Log.e("QuizActivity", "Invalid question data. Skipping...")
            return
        }

        questionTV.text = HtmlCompat.fromHtml(result.question, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

        val optionsArray = listOf(
            result.incorrect_answers[0],
            result.incorrect_answers[1],
            result.incorrect_answers[2],
            result.correct_answer
        ).shuffled()

        option1Btn.text = HtmlCompat.fromHtml(optionsArray[0], HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        option2Btn.text = HtmlCompat.fromHtml(optionsArray[1], HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        option3Btn.text = HtmlCompat.fromHtml(optionsArray[2], HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        option4Btn.text = HtmlCompat.fromHtml(optionsArray[3], HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

        setOptionListener(option1Btn, result.correct_answer)
        setOptionListener(option2Btn, result.correct_answer)
        setOptionListener(option3Btn, result.correct_answer)
        setOptionListener(option4Btn, result.correct_answer)
    }

    private fun setOptionListener(option: Button, correctAnswer: String) {
        option.setOnClickListener {
            disableOptionClickListener()

            if (option.text.toString() == correctAnswer) {
                option.setBackgroundColor(Color.GREEN)
                totalCorrectAnswer++
            } else {
                option.setBackgroundColor(Color.RED)
                highlightCorrectAnswer(correctAnswer)
            }
        }
    }

    private fun highlightCorrectAnswer(correctAnswer: String) {
        listOf(option1Btn, option2Btn, option3Btn, option4Btn).forEach {
            if (it.text.toString() == correctAnswer) {
                it.setBackgroundColor(Color.GREEN)
            }
        }
    }

    private fun disableOptionClickListener() {
        listOf(option1Btn, option2Btn, option3Btn, option4Btn).forEach {
            it.isClickable = false
        }
    }

    private fun resetOptionBackground() {
        listOf(option1Btn, option2Btn, option3Btn, option4Btn).forEach {
            it.setBackgroundColor(Color.WHITE)
            it.isClickable = true
        }
    }

    private fun init() {
        repo = Repo(RetrofitBuilder.getInstace())
        viewModelFactory = ViewModelFactory(repo)
        viewModel = ViewModelProvider(this, viewModelFactory)[ViewModel::class.java]
    }
}
