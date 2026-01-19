package com.angel.maths_games.Sub

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.angel.maths_games.Addition.Practce_Activity
import com.angel.maths_games.databinding.ActivitySubDuelBinding
import com.angel.maths_games.databinding.ActivitySubSecondOptBinding

class Sub_Second_opt_Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySubSecondOptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySubSecondOptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.practice.setOnClickListener {
            startActivity(Intent(this, Sub_Practice_Activity::class.java))

        }
        binding.timer.setOnClickListener {
            startActivity(Intent(this, Sub_Timer_Activity::class.java))

        }
        binding.duel.setOnClickListener {
            startActivity(Intent(this, Sub_Duel_Activity::class.java))

        }
        binding.hard.setOnClickListener {
            startActivity(Intent(this, Sub_hard_Activity::class.java))

        }


    }
}