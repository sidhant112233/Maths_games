package com.angel.maths_games.Mul

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.angel.maths_games.R
import com.angel.maths_games.databinding.ActivityMulDuelBinding

class Mul_Duel_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityMulDuelBinding
    private lateinit var questionText: TextView
    private lateinit var timerText: TextView
    private lateinit var turnIndicator: TextView
    private lateinit var player1Score: TextView
    private lateinit var player2Score: TextView
    private lateinit var winnerText: TextView
    private lateinit var finalScoreText: TextView
    private lateinit var buttons: List<Button>


    private var currentPlayer = 1
    private var player1Points = 0
    private var player2Points = 0
    private var currentCorrectAnswer = 0
    private val totalQuestions = 10
    private var currentQuestionIndex = 0


    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 30000
    private val timerInterval: Long = 1000
    private var isTimerRunning = false


    private val questions = mutableListOf<Question>()

    data class Question(
        val num1: Int,
        val num2: Int,
        val correctAnswer: Int,
        val options: List<Int>,
        val questionText: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMulDuelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        generateAllQuestions()
        loadQuestion(currentQuestionIndex)
        setupClickListeners()
    }

    private fun initializeViews() {
        questionText = findViewById(R.id.questionText)
        timerText = findViewById(R.id.timerText)
        turnIndicator = findViewById(R.id.turnIndicator)
        player1Score = findViewById(R.id.player1Score)
        player2Score = findViewById(R.id.player2Score)
        winnerText = findViewById(R.id.winnerText)
        finalScoreText = findViewById(R.id.finalScoreText)

        buttons = listOf(
            findViewById(R.id.btnOption1),
            findViewById(R.id.btnOption2),
            findViewById(R.id.btnOption3),
            findViewById(R.id.btnOption4)
        )

        binding.successMessage.visibility = View.GONE
        binding.btnNextQuestion.visibility = View.GONE
        binding.gameOverLayout.visibility = View.GONE
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            showExitConfirmation()
        }

        buttons.forEach { btn ->
            btn.setOnClickListener {
                val selectedAnswer = btn.text.toString().toIntOrNull()
                val isCorrect = selectedAnswer == currentCorrectAnswer

                if (isCorrect) {
                    handleCorrectAnswer(btn)
                } else {
                    handleWrongAnswer(btn)
                }
            }
        }

        binding.btnNextQuestion.setOnClickListener {
            switchPlayerTurn()
            currentQuestionIndex++
            if (currentQuestionIndex < totalQuestions) {
                loadQuestion(currentQuestionIndex)
                binding.successMessage.visibility = View.GONE
                binding.btnNextQuestion.visibility = View.GONE
                resetAllButtons()
            } else {
                endGame()
            }
        }

        binding.btnPlayAgain.setOnClickListener {
            resetGame()
        }

        binding.btnExitDuel.setOnClickListener {
            finish()
        }
    }

    private fun handleCorrectAnswer(button: Button) {
        stopTimer()
        showCorrectEffect(button)

        if (currentPlayer == 1) {
            player1Points++
            player1Score.text = player1Points.toString()
            binding.successMessage.text = "🎉 Player 1 got it right! +1 point"
        } else {
            player2Points++
            player2Score.text = player2Points.toString()
            binding.successMessage.text = "🎉 Player 2 got it right! +1 point"
        }

        disableAllButtons()
        highlightCorrectAnswer(currentCorrectAnswer)

        binding.successMessage.visibility = View.VISIBLE
        binding.btnNextQuestion.visibility = View.VISIBLE
    }

    private fun handleWrongAnswer(button: Button) {
        showWrongEffect(button)

        switchPlayerTurn()
        binding.btnNextQuestion.visibility = View.GONE
        binding.successMessage.text = "❌ Wrong answer! Turn goes to Player $currentPlayer"
        binding.successMessage.visibility = View.VISIBLE

        button.postDelayed({
            resetAllButtons()
            binding.successMessage.visibility = View.GONE
            updateTurnIndicator()
        }, 1500)
    }

    private fun switchPlayerTurn() {
        currentPlayer = if (currentPlayer == 1) 2 else 1
        updateTurnIndicator()
    }

    private fun updateTurnIndicator() {
        turnIndicator.text = "Player $currentPlayer's Turn!"
        turnIndicator.setBackgroundColor(
            if (currentPlayer == 1) getColor(R.color.player1_color)
            else getColor(R.color.player2_color)
        )
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
        timeLeftInMillis = 30000
        updateTimerText()
    }

    private fun updateTimerText() {
        val seconds = (timeLeftInMillis / 1000).toInt()
        timerText.text = "${seconds}s"
    }

    private fun onTimeUp() {
        disableAllButtons()
        switchPlayerTurn()
        binding.successMessage.text = "⏰ Time's up! Turn goes to Player $currentPlayer"
        binding.successMessage.visibility = View.VISIBLE

        binding.successMessage.postDelayed({
            currentQuestionIndex++
            if (currentQuestionIndex < totalQuestions) {
                loadQuestion(currentQuestionIndex)
                binding.successMessage.visibility = View.GONE
                resetAllButtons()
            } else {
                endGame()
            }
        }, 2000)
    }


    private fun generateAllQuestions() {
        questions.clear()
        for (i in 0 until totalQuestions) {
            generateMultiplicationQuestion()
        }
    }

    private fun generateMultiplicationQuestion() {
        val num1 = (2..12).random()
        val num2 = (2..12).random()
        val correctAnswer = num1 * num2
        val options = generateOptions(correctAnswer)
        val questionText = "$num1 × $num2 = ?"
        questions.add(Question(num1, num2, correctAnswer, options, questionText))
    }

    private fun generateOptions(correctAnswer: Int): List<Int> {
        val options = mutableListOf(correctAnswer)
        while (options.size < 4) {
            val deviation = listOf(-12, -9, -6, -3, 3, 6, 9, 12).random()
            val wrongOption = correctAnswer + deviation
            if (wrongOption > 0 &&
                wrongOption != correctAnswer &&
                !options.contains(wrongOption)
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

            resetTimer()
            startTimer()
            questionText.text = question.questionText

            buttons.forEachIndexed { i, btn ->
                btn.text = question.options[i].toString()
                btn.setBackgroundResource(R.drawable.button_normal_background)
                btn.isEnabled = true
            }

            updateTurnIndicator()
        }
    }

    private fun showCorrectEffect(button: Button) {
        button.setBackgroundResource(R.drawable.button_correct_background)
        button.animate().scaleX(1.1f).scaleY(1.1f).setDuration(300).withEndAction {
            button.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
        }.start()
    }

    private fun showWrongEffect(button: Button) {
        button.setBackgroundResource(R.drawable.button_wrong_background)
        button.animate().translationX(30f).setDuration(100).withEndAction {
            button.animate().translationX(-30f).setDuration(100).withEndAction {
                button.animate().translationX(0f).setDuration(100).start()
            }.start()
        }.start()
    }

    private fun endGame() {
        stopTimer()
        binding.mainGameLayout.visibility = View.GONE
        binding.gameOverLayout.visibility = View.VISIBLE

        val winner = when {
            player1Points > player2Points -> "🏆 Player 1 Wins!"
            player2Points > player1Points -> "🏆 Player 2 Wins!"
            else -> "🤝 It's a Tie!"
        }

        winnerText.text = winner
        finalScoreText.text = "Player 1: $player1Points | Player 2: $player2Points"
    }

    private fun resetGame() {
        currentPlayer = 1
        player1Points = 0
        player2Points = 0
        currentQuestionIndex = 0

        player1Score.text = "0"
        player2Score.text = "0"

        binding.gameOverLayout.visibility = View.GONE
        binding.mainGameLayout.visibility = View.VISIBLE
        binding.successMessage.visibility = View.GONE
        binding.btnNextQuestion.visibility = View.GONE

        generateAllQuestions()
        loadQuestion(currentQuestionIndex)
        resetAllButtons()
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
        buttons.forEach { btn -> btn.isEnabled = false }
    }

    private fun showExitConfirmation() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Exit Game")
            .setMessage("Are you sure you want to exit the duel? Progress will be lost.")
            .setPositiveButton("Exit") { _, _ -> finish() }
            .setNegativeButton("Cancel", null)
            .setCancelable(true)
            .show()
    }

    private var backPressedTime = 0L
    override fun onBackPressed() {
        super.onBackPressed()

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            android.widget.Toast.makeText(this, "Press back again to exit", android.widget.Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
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
}
