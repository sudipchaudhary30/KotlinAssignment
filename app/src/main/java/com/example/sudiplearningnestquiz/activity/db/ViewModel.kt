package com.example.sudiplearningnestquiz.activity.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizViewModel(private val repo: Repo) : ViewModel() {

    private val _quizLiveData = MutableLiveData<QuizStructure>()
    val quizLiveData: LiveData<QuizStructure> get() = _quizLiveData

    fun getQuiz(amount: Int, category: Int, difficulty: String) {
        val call = repo.getQuiz(amount, category, difficulty)
        call.enqueue(object : Callback<QuizStructure> {
            override fun onResponse(call: Call<QuizStructure>, response: Response<QuizStructure>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.i("QuizViewModel", "Quiz Data Received: $it")
                        _quizLiveData.postValue(it)
                    } ?: Log.e("QuizViewModel", "Response body is null")
                } else {
                    Log.e("QuizViewModel", "API Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<QuizStructure>, t: Throwable) {
                Log.e("QuizViewModel", "Failed to fetch quiz data", t)
            }
        })
    }
}
