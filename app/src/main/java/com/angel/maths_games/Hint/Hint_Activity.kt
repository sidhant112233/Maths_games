package com.angel.maths_games.Hint

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angel.maths_games.MainActivity
import com.angel.maths_games.R
import com.angel.maths_games.databinding.ActivityHintBinding

class Hint_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityHintBinding
    private lateinit var adapter: ViewPager_Adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ViewPager_Adapter(this)
        binding.viewPager.adapter = adapter


        binding.btnNext.setOnClickListener {
            val current = binding.viewPager.currentItem
            if (current < 2) {
                binding.viewPager.currentItem = current + 1
            } else {

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }


        binding.btnSkip.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}