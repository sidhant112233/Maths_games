package com.angel.maths_games

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.fragment.app.DialogFragment
import com.angel.maths_games.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.logging.Handler

class   MainActivity : AppCompatActivity() {
    val TAG: String = "Testing Ads"


        private var interstitialAd: InterstitialAd? = null
        private var adRequest: AdRequest? = null
        private var loadingDialog: android.app.AlertDialog? = null
        private var pendingIntent: Intent? = null

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {
            adRequest = AdRequest.Builder().build()
            loadfullscreenadd()
            loadBannerAd()
        }

        binding.add.setOnClickListener {
            showAdWithAction {
                val intent = Intent(this, Second_opt_Activity::class.java)
                intent.putExtra("MAth_GAME", "Addition")
                startActivity(intent)
            }
        }

        binding.sub.setOnClickListener {
            showAdWithAction {
                val intent = Intent(this, Second_opt_Activity::class.java)
                intent.putExtra("MAth_GAME", "Subtraction")
                startActivity(intent)
            }
        }

        binding.multiply.setOnClickListener {
            showAdWithAction {
                val intent = Intent(this@MainActivity, Second_opt_Activity::class.java)
                intent.putExtra("MAth_GAME", "Multiplication")
                startActivity(intent)
            }
        }

        binding.div.setOnClickListener {
            showAdWithAction {
                val intent = Intent(this@MainActivity, Second_opt_Activity::class.java)
                intent.putExtra("MAth_GAME", "Division")
                startActivity(intent)
            }
        }

        binding.setting.setOnClickListener {
            showAdWithAction {
                startActivity(Intent(this@MainActivity, Setting_Activity::class.java))
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
                this@MainActivity,
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
                         android.os.Handler(Looper.getMainLooper()).postDelayed({
                             loadfullscreenadd()
                         }, 5000)
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

    override fun onBackPressed() {
        BottomDialog().show(supportFragmentManager, "bottomDialog")
    }




}
class BottomDialog : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.rate_dialog, container, false)

        val laterBtn = view.findViewById<Button>(R.id.laterBtn)
        val rateBtn = view.findViewById<Button>(R.id.rateBtn)

        laterBtn.setOnClickListener {
            requireActivity().finish()
            dismiss()
        }

        rateBtn.setOnClickListener {
            openPlayStore()
            dismiss()
        }

        return view
    }

    private fun openPlayStore() {
        val appPackage = requireActivity().packageName

        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackage")
                )
            )
        } catch (e: Exception) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackage")
                )
            )
        }
    }
}

