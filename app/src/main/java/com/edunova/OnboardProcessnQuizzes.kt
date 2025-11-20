package com.edunova

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.edunova.R
import com.edunova.ai.GeminiHelper
import kotlinx.coroutines.*

class OnboardingProcessnQuizzes : AppCompatActivity() {

    private lateinit var quizText: TextView
    private lateinit var generateButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard_processn_quizzes)

        quizText = findViewById(R.id.quizTextView)
        generateButton = findViewById(R.id.generateQuizBtn)

        // Generate quiz when the screen loads
        generateQuiz()

        // Allow user to generate another quiz
        generateButton.setOnClickListener {
            generateQuiz()
        }
    }

    private fun generateQuiz() {
        quizText.text = "Generating quiz... Please wait."

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val model = GeminiHelper.getModel()
                val prompt = """
                    Generate 5 random multiple-choice quiz questions on basic Java programming.
                    Format them clearly as:
                    Q1. ...
                    A) ...
                    B) ...
                    C) ...
                    D) ...
                    Answer: ...
                """.trimIndent()

                val response = model.generateContent(prompt)
                val quizOutput = response.text ?: "No quiz generated."

                withContext(Dispatchers.Main) {
                    quizText.text = quizOutput
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    quizText.text = "Error generating quiz: ${e.message}"
                }
            }
        }
    }
}
