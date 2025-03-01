package com.example.sudiplearningnestquiz

import org.junit.Test
import org.junit.Assert.*

class QuizUnitTest {

    @Test
    fun testCalculateScore() {
        val quiz = Quiz()
        quiz.addQuestion(Question("What is 2 + 2?", listOf("3", "4", "5", "6"), "4"))
        quiz.addQuestion(Question("What is the capital of France?", listOf("Berlin", "Madrid", "Paris", "London"), "Paris"))

        val score = quiz.calculateScore(listOf("4", "Paris"))

        assertEquals(2, score) // assuming the correct answers were provided
    }

    @Test
    fun testInvalidAnswer() {
        val quiz = Quiz()
        quiz.addQuestion(Question("What is 2 + 2?", listOf("3", "4", "5", "6"), "4"))

        val score = quiz.calculateScore(listOf("3"))

        assertEquals(0, score) // incorrect answer
    }

    @Test
    fun testEmptyAnswers() {
        val quiz = Quiz()
        quiz.addQuestion(Question("What is 2 + 2?", listOf("3", "4", "5", "6"), "4"))

        val score = quiz.calculateScore(emptyList())

        assertEquals(0, score) // no answers provided
    }
}
