package com.angel.maths_games

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.angel.maths_games.R
import com.angel.maths_games.databinding.ActivityTimerBinding

class Timer_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityTimerBinding
    private lateinit var questionText: TextView
    private lateinit var timerText: TextView
    private lateinit var buttons: List<Button>

    private var wrongAnswerCount = 0
    private val maxWrongAttempts = 3
    private var currentCorrectAnswer = 0
    private var currentQuestionNumber = 0
    private val totalQuestions = 10
    private var score = 0


    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 20000
    private val timerInterval: Long = 1000
    private var isTimerRunning = false


    private val questions = mutableListOf<Question>()
    private var currentQuestionIndex = 0

    data class Question(
        val num1: Int,
        val num2: Int,
        val correctAnswer: Int,
        val options: List<Int>
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        questionText = findViewById(R.id.questionText)
        timerText = findViewById(R.id.timerText)
        buttons = listOf(
            findViewById(R.id.btnOption1),
            findViewById(R.id.btnOption2),
            findViewById(R.id.btnOption3),
            findViewById(R.id.btnOption4)
        )


        binding.tryAgainLayout.visibility = View.GONE
        binding.successMessage.visibility = View.GONE
        binding.btnNextQuestion.visibility = View.GONE


        generateAllQuestions()


        loadQuestion(currentQuestionIndex)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        buttons.forEach { btn ->
            btn.setOnClickListener {
                val selectedAnswer = btn.text.toString().toIntOrNull()
                val isCorrect = selectedAnswer == currentCorrectAnswer

                if (isCorrect) {
                    score++
                    stopTimer()
                    showCorrectEffect(btn)
                } else {
                    wrongAnswerCount++
                    showWrongEffect(btn)

                    if (wrongAnswerCount >= maxWrongAttempts) {
                        stopTimer()
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
            currentQuestionIndex++
            if (currentQuestionIndex < totalQuestions) {
                loadQuestion(currentQuestionIndex)
                binding.successMessage.visibility = View.GONE
                binding.btnNextQuestion.visibility = View.GONE
                resetAllButtons()
            } else {
                showFinalScore()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startTimer()
    }

    override fun onPause() {
        super.onPause()
        stopTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

    private fun startTimer() {
        if (isTimerRunning) return

        countDownTimer = object : CountDownTimer(timeLeftInMillis, timerInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                timeLeftInMillis = 0
                updateTimerText()
                onTimeUp()
            }
        }.start()

        isTimerRunning = true
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        isTimerRunning = false
    }

    private fun resetTimer() {
        stopTimer()
        timeLeftInMillis = 20000
        updateTimerText()
    }

    private fun updateTimerText() {
        val seconds = (timeLeftInMillis / 1000).toInt()
        val timeFormatted = String.format("%02d", seconds)
        timerText.text = "Time: ${timeFormatted}s"

    }

    private fun onTimeUp() {
        disableAllButtons()
        showTryAgainScreen()
        binding.tryAgainMessage.text = "⏰ Time's up! Would you like to try again?"
    }

    private fun generateAllQuestions() {
        questions.clear()
        for (i in 0 until totalQuestions) {
            val num1 = (2..12).random()
            val num2 = (2..12).random()
            val correctAnswer = num1 + num2
            val options = generateOptions(correctAnswer)
            questions.add(Question(num1, num2, correctAnswer, options))
        }
    }

    private fun generateOptions(correctAnswer: Int): List<Int> {
        val options = mutableListOf(correctAnswer)

        while (options.size < 4) {
            val deviation = listOf(-8, -6, -4, -2, 2, 4, 6, 8).random()
            val wrongOption = correctAnswer + deviation

            if (wrongOption > 0 &&
                wrongOption != correctAnswer &&
                !options.contains(wrongOption) &&
                wrongOption <= 24
            ) {
                options.add(wrongOption)
            }
        }

        return options.shuffled()
    }

    private fun loadQuestion(index: Int) {
        if (index < questions.size) {
            val question = questions[index]
            currentCorrectAnswer = question.correctAnswer
            currentQuestionNumber = index + 1


            resetTimer()
            startTimer()


            questionText.text = getString(R.string.what_is, question.num1, question.num2)


            buttons.forEachIndexed { buttonIndex, button ->
                button.text = question.options[buttonIndex].toString()
                button.setBackgroundResource(R.drawable.button_normal_background)
                button.isEnabled = true
            }


            wrongAnswerCount = 0
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
        highlightCorrectAnswer(currentCorrectAnswer)


        binding.successMessage.visibility = View.VISIBLE
        binding.btnNextQuestion.visibility = View.VISIBLE


        if (currentQuestionIndex == totalQuestions - 1) {
            binding.successMessage.text = getString(R.string.perfect_final_question_completed)
            binding.btnNextQuestion.text = getString(R.string.show_results)
        } else {
            binding.successMessage.text = getString(R.string.correct_ready_for_next_question)
            binding.btnNextQuestion.text = getString(R.string.next_question)
        }
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
            stopTimer()
            highlightCorrectAnswer(currentCorrectAnswer)
            disableAllButtons()


            button.postDelayed({
                showTryAgainScreen()
            }, 1000)
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
            "You've used all $maxWrongAttempts attempts on question $currentQuestionNumber. Would you like to try again?"
        disableAllButtons()
    }

    private fun resetGame() {
        wrongAnswerCount = 0
        binding.tryAgainLayout.visibility = View.GONE
        binding.mainGameLayout.visibility = View.VISIBLE
        binding.successMessage.visibility = View.GONE
        binding.btnNextQuestion.visibility = View.GONE


        loadQuestion(currentQuestionIndex)
        resetAllButtons()
    }

    private fun showFinalScore() {
        stopTimer()
        binding.mainGameLayout.visibility = View.GONE
        binding.tryAgainLayout.visibility = View.VISIBLE


        binding.tryAgainMessage.text =
            "🎊 Quiz Completed!\n\n"
        binding.btnTryAgain.text = "Play Again"
        binding.btnExit.text = "Main Menu"


        currentQuestionIndex = 0
        score = 0
    }

    private fun resetAllButtons() {
        buttons.forEach { btn ->
            btn.isEnabled = true
            btn.setBackgroundResource(R.drawable.button_normal_background)
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
        buttons.forEach { btn ->
            btn.isEnabled = false
        }
    }
}