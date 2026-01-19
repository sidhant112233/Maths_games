package com.angel.maths_games.Hint

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import com.angel.maths_games.R

class Third_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_third_, container, false)

        val progressFill = view.findViewById<View>(R.id.progressFill)
        val trophyIcon = view.findViewById<View>(R.id.trophyIcon)


        progressFill.post {
            val parentWidth = (progressFill.parent as View).width
            val animator = ValueAnimator.ofInt(0, parentWidth)
            animator.duration = 2000
            animator.repeatCount = ValueAnimator.INFINITE
            animator.repeatMode = ValueAnimator.RESTART
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.addUpdateListener {
                val value = it.animatedValue as Int
                val params = progressFill.layoutParams
                params.width = value
                progressFill.layoutParams = params
            }
            animator.start()
        }


        val bounceAnimator = ObjectAnimator.ofFloat(trophyIcon, "translationY", 0f, -30f, 0f)
        bounceAnimator.duration = 1200
        bounceAnimator.repeatCount = ObjectAnimator.INFINITE
        bounceAnimator.interpolator = AccelerateDecelerateInterpolator()
        bounceAnimator.start()

        val rotateAnimator = ObjectAnimator.ofFloat(trophyIcon, "rotation", 0f, 10f, 0f, -10f, 0f)
        rotateAnimator.duration = 1800
        rotateAnimator.repeatCount = ObjectAnimator.INFINITE
        rotateAnimator.interpolator = AccelerateDecelerateInterpolator()
        rotateAnimator.start()

        return view
    }
}
