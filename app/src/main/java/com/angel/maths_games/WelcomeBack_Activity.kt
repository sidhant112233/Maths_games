package com.angel.maths_games

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.angel.maths_games.databinding.ActivityWelcomeBackBinding

class WelcomeBack_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBackBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bounce)

        binding.emoji.startAnimation(bounce)
        binding.welcomeText.startAnimation(fadeIn)
        binding.userName.startAnimation(fadeIn)
        binding.continueBtn.startAnimation(bounce)

        binding.continueBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
