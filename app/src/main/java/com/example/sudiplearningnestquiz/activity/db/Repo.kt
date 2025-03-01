package com.example.sudiplearningnestquiz.activity.db

class Repo(private val quizInterface: QuizInterface) {


    fun getQuiz(amount: Int, category: Int, difficulty: String) =
        quizInterface.getQuiz(amount, category, difficulty)
}
