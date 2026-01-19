package com.angel.maths_games

import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.angel.maths_games.databinding.ActivitySettingBinding

class Setting_Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private var isMuted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnBack.setOnClickListener { onBackPressed() }

        binding.btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey! Check out this fun Math Game app: https://play.google.com/store/apps/details?id=$packageName"
            )
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        binding.btnRate.setOnClickListener {
            val uri = Uri.parse("market://details?id=$packageName")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(goToMarket)
            } catch (e: Exception) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }




        binding.btnPrivacy.setOnClickListener {
            val privacyUrl = "https://sidhant112233.github.io/Math-Game-Privacy/"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl))
            startActivity(browserIntent)
        }
    }
}
