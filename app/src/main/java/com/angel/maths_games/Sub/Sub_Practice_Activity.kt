package com.angel.maths_games.Sub

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.angel.maths_games.R
import com.angel.maths_games.databinding.ActivitySubPracticeBinding

class Sub_Practice_Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySubPracticeBinding
    private lateinit var questionText: TextView
    private lateinit var buttons: List<Button>
    private var wrongAnswerCount = 0
    private val maxWrongAttempts = 3
    private var currentCorrectAnswer = 0

    private val bg_list = listOf(
        R.drawable.card_bg_blue,
        R.drawable.card_bg_green,
        R.drawable.card_bg_purple,
        R.drawable.card_bg_red
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        questionText = findViewById(R.id.questionText)
        buttons = listOf(
            findViewById(R.id.btnOption1),
            findViewById(R.id.btnOption2),
            findViewById(R.id.btnOption3),
            findViewById(R.id.btnOption4)
        )

        // Hide layouts initially
        binding.tryAgainLayout.visibility = View.GONE
        binding.successMessage.visibility = View.GONE

        // First question
        generateNewQuestion()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        buttons.forEach { btn ->
            btn.setOnClickListener {
                val isCorrect = btn.text.toString().toIntOrNull() == currentCorrectAnswer
                if (isCorrect) {
                    showCorrectEffect(btn)
                } else {
                    wrongAnswerCount++
                    showWrongEffect(btn)
                    if (wrongAnswerCount >= maxWrongAttempts) {
                        showTryAgainScreen()
                    } else {
                        resetWrongButtonAfterDelay(btn)
                    }
                }
            }
        }

        binding.btnTryAgain.setOnClickListener {
            resetGame()
        }

        binding.btnExit.setOnClickListener {
            finish()
        }

        binding.btnNextQuestion.setOnClickListener {
            generateNewQuestion()
            binding.successMessage.visibility = View.GONE
            resetAllButtons()
        }
    }

    private fun showCorrectEffect(button: Button) {
        button.setBackgroundResource(R.drawable.button_correct_background)
        button.animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(300)
            .withEndAction {
                button.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(200)
                    .start()
            }.start()

        disableAllButtons()

        binding.successMessage.visibility = View.VISIBLE
        binding.btnNextQuestion.visibility = View.VISIBLE
        highlightCorrectAnswer(currentCorrectAnswer)
    }

    private fun showWrongEffect(button: Button) {
        button.setBackgroundResource(R.drawable.button_wrong_background)
        button.animate()
            .translationX(30f)
            .setDuration(100)
            .withEndAction {
                button.animate()
                    .translationX(-30f)
                    .setDuration(100)
                    .withEndAction {
                        button.animate()
                            .translationX(0f)
                            .setDuration(100)
                            .start()
                    }.start()
            }.start()

        if (wrongAnswerCount >= maxWrongAttempts) {
            highlightCorrectAnswer(currentCorrectAnswer)
        }
    }

    private fun resetWrongButtonAfterDelay(button: Button) {
        button.postDelayed({
            button.setBackgroundResource(R.drawable.button_normal_background)
        }, 1000)
    }

    private fun showTryAgainScreen() {
        binding.mainGameLayout.visibility = View.GONE
        binding.tryAgainLayout.visibility = View.VISIBLE
        binding.tryAgainMessage.text =
            getString(
                R.string.you_ve_used_all_attempts_would_you_like_to_try_again,
                maxWrongAttempts
            )
        disableAllButtons()
    }

    private fun resetGame() {
        wrongAnswerCount = 0
        binding.tryAgainLayout.visibility = View.GONE
        binding.mainGameLayout.visibility = View.VISIBLE
        binding.successMessage.visibility = View.GONE
        binding.btnNextQuestion.visibility = View.GONE
        resetAllButtons()
        generateNewQuestion()
    }

    private fun resetAllButtons() {
        buttons.forEach { btn ->
            btn.isEnabled = true
            btn.setBackgroundResource(R.drawable.button_normal_background)
        }
    }

    private fun generateNewQuestion() {
        wrongAnswerCount = 0
        binding.btnNextQuestion.visibility = View.GONE

        // ✅ Generate subtraction question
        val num1 = (5..20).random()
        val num2 = (1..num1).random() // ensures positive result
        currentCorrectAnswer = num1 - num2

        questionText.text = getString(R.string.what_is_sub, num1, num2)

        // Animation
        questionText.scaleX = 0.8f
        questionText.scaleY = 0.8f
        questionText.alpha = 0f
        questionText.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(400)
            .start()

        resetAllButtons()
        generateOptions(currentCorrectAnswer)
    }

    private fun generateOptions(correctAnswer: Int) {
        val options = mutableListOf(correctAnswer)
        while (options.size < 4) {
            val deviation = listOf(-8, -6, -4, -2, 2, 4, 6, 8).random()
            val wrongOption = correctAnswer + deviation
            if (wrongOption >= 0 && wrongOption != correctAnswer && !options.contains(wrongOption)) {
                options.add(wrongOption)
            }
        }

        options.shuffle()
        val shuffledBackgrounds = bg_list.shuffled()

        buttons.forEachIndexed { index, button ->
            button.text = options[index].toString()
            button.isEnabled = true
            button.setBackgroundResource(shuffledBackgrounds[index % bg_list.size])

            button.alpha = 0f
            button.animate()
                .alpha(1f)
                .setDuration(300)
                .setStartDelay(index * 100L)
                .start()
        }
    }

    private fun highlightCorrectAnswer(correctAnswer: Int) {
        buttons.forEach { btn ->
            if (btn.text.toString().toIntOrNull() == correctAnswer) {
                btn.setBackgroundResource(R.drawable.button_correct_background)
            }
        }
    }

    private fun disableAllButtons() {
        buttons.forEach { it.isEnabled = false }
    }
}
