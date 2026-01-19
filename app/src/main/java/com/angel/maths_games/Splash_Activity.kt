package com.angel.maths_games

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.angel.maths_games.Hint.Hint_Activity
import com.angel.maths_games.databinding.ActivitySplashBinding

class Splash_Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.logo.startAnimation(fadeIn)
        binding.appName.startAnimation(fadeIn)


        val sharedPref = getSharedPreferences("MathGamePrefs", MODE_PRIVATE)
        val isFirstTime = sharedPref.getBoolean("isFirstTime", true)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isFirstTime) {
                sharedPref.edit().putBoolean("isFirstTime", false).apply()
                startActivity(Intent(this, Hint_Activity::class.java))
            } else {
                startActivity(Intent(this, WelcomeBack_Activity::class.java))
            }
            finish()
        }, 2500)

    }
}
