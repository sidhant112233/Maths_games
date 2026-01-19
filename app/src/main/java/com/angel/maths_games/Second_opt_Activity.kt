package com.angel.maths_games

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.angel.maths_games.Addition.Duel_Activity
import com.angel.maths_games.Addition.Hard_Activity
import com.angel.maths_games.Addition.Practce_Activity
import com.angel.maths_games.Div.DIv_Duel_Activity
import com.angel.maths_games.Div.Div_Hard_Activity
import com.angel.maths_games.Div.Div_Practice_Activity
import com.angel.maths_games.Div.Div_Timer_Activity
import com.angel.maths_games.Mul.Mul_Duel_Activity
import com.angel.maths_games.Mul.Mul_Hard_Activity
import com.angel.maths_games.Mul.Mul_Practice_Activity
import com.angel.maths_games.Mul.Mul_Timer_Activity
import com.angel.maths_games.Sub.Sub_Duel_Activity
import com.angel.maths_games.Sub.Sub_Practice_Activity
import com.angel.maths_games.Sub.Sub_Timer_Activity
import com.angel.maths_games.Sub.Sub_hard_Activity
import com.angel.maths_games.databinding.ActivitySecondOptBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class Second_opt_Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondOptBinding
    private var interstitialAd: InterstitialAd? = null
    private var adRequest: AdRequest? = null
    private var loadingDialog: android.app.AlertDialog? = null
    private var pendingIntent: Intent? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondOptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener {
            onBackPressed()
        }

        MobileAds.initialize(this) {
            adRequest = AdRequest.Builder().build()
            loadfullscreenadd()
            loadBannerAd();
        }

        val intent = intent
        val type = intent.getStringExtra("MAth_GAME")


        binding.title.text = type
       binding.practice.setOnClickListener {
            when (type) {
                "Addition" -> {
                    showAdWithAction {

                        val intent = Intent(this, Practce_Activity::class.java)
                        startActivity(intent)
                    }
                }

                "Subtraction" -> {
                    showAdWithAction {
                        val intent = Intent(this, Sub_Practice_Activity::class.java)

                        startActivity(intent)

                    }
                }

                "Division" -> {
                    showAdWithAction {
                        val intent = Intent(this, Div_Practice_Activity::class.java)

                        startActivity(intent)

                    }
                }

                "Multiplication" -> {
                    showAdWithAction {
                        val intent = Intent(this, Mul_Practice_Activity::class.java)

                        startActivity(intent)

                    }
                }
            }

        }
        binding.timer.setOnClickListener {
            when (type) {
                "Addition" -> {
                    showAdWithAction {
                        val intent = Intent(this, Timer_Activity::class.java)

                        startActivity(intent)

                    }
                }

                "Subtraction" -> {
                    showAdWithAction {
                        val intent = Intent(this, Sub_Timer_Activity::class.java)

                        startActivity(intent)

                    }
                }

                "Division" -> {
                    showAdWithAction {
                        val intent = Intent(this, Div_Timer_Activity::class.java)

                        startActivity(intent)

                    }
                }

                "Multiplication" -> {
                    showAdWithAction {
                        val intent = Intent(this, Mul_Timer_Activity::class.java)

                        startActivity(intent)

                    }
                }
            }
        }

        binding.duel.setOnClickListener {
            when (type) {
                "Addition" -> {
                    showAdWithAction {

                        val intent = Intent(this, Duel_Activity::class.java)
                        startActivity(intent)
                    }
                }

                "Subtraction" -> {
                    showAdWithAction {

                        val intent = Intent(this, Sub_Duel_Activity::class.java)
                        startActivity(intent)
                    }
                }

                "Division" -> {
                    showAdWithAction {

                        val intent = Intent(this, DIv_Duel_Activity::class.java)
                        startActivity(intent)
                    }
                }

                "Multiplication" -> {
                    showAdWithAction {

                        val intent = Intent(this, Mul_Duel_Activity::class.java)
                        startActivity(intent)
                    }
                }
            }

        }
        binding.hard.setOnClickListener {
            when (type) {
                "Addition" -> {
                    showAdWithAction {

                        val intent = Intent(this, Hard_Activity::class.java)
                        startActivity(intent)
                    }
                }

                "Subtraction" -> {
                    showAdWithAction {

                        val intent = Intent(this, Sub_hard_Activity::class.java)
                        startActivity(intent)
                    }
                }

                "Division" -> {
                    showAdWithAction {

                        val intent = Intent(this, Div_Hard_Activity::class.java)
                        startActivity(intent)
                    }
                }

                "Multiplication" -> {
                    showAdWithAction {

                        val intent = Intent(this, Mul_Hard_Activity::class.java)
                        startActivity(intent)
                    }
                }
            }

        }
    }

    private fun showAdWithAction(action: () -> Unit) {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    action()
                    loadfullscreenadd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    action()
                    loadfullscreenadd()
                }
            }
            interstitialAd?.show(this)
        } else {
            showLoadingDialog()

            val startTime = System.currentTimeMillis()
           loadfullscreenadd()

            val handler = android.os.Handler(mainLooper)
            val checkTask = object : Runnable {
                override fun run() {
                    val elapsed = System.currentTimeMillis() - startTime

                    if (interstitialAd != null) {
                        hideLoadingDialog()
                        showAdWithAction(action)
                    } else if (elapsed >= 20_000) {
                        hideLoadingDialog()
                        action()
                    } else {
                        handler.postDelayed(this, 500)
                    }
                }
            }

            handler.post(checkTask)
        }
    }


    private fun loadfullscreenadd() {
        adRequest?.let {
            InterstitialAd.load(
                this,
                getString(R.string.interstitial_ad_unit_id),
                it,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: InterstitialAd) {
                        Log.d("TAG", "Ad was loaded.")
                        interstitialAd = ad
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d("TAG", adError.message)
                        interstitialAd = null

                    }

                },
            )
        }

    }

    private fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = android.app.AlertDialog.Builder(this)
                .setMessage("Loading ad, please wait…")
                .setCancelable(false)
                .create()
        }
        loadingDialog?.show()
    }

    private fun hideLoadingDialog() {
        loadingDialog?.dismiss()
    }

    private fun loadBannerAd() {
        val adView = AdView(this)
        adView.adUnitId = getString(R.string.banner_ad_unit_id)
        adView.setAdSize(AdSize.BANNER)


        binding.banner.removeAllViews()


        binding.banner.addView(adView)

        val request = AdRequest.Builder().build()
        adView.loadAd(request)
    }
}