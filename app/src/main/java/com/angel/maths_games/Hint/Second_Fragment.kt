package com.angel.maths_games.Hint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.angel.maths_games.R

class Second_Fragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second_, container, false)
        val symbolX = view.findViewById<TextView>(R.id.symbolX)
        val plus = view.findViewById<TextView>(R.id.plus)
        val minus = view.findViewById<TextView>(R.id.minus)
        val div = view.findViewById<TextView>(R.id.div)
        val floatAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.float_symbol3)
        val floatAnim2 = AnimationUtils.loadAnimation(requireContext(), R.anim.float_symbol2)
        val floatAnim23 = AnimationUtils.loadAnimation(requireContext(), R.anim.float_symbol1)
        val floatAnim234 = AnimationUtils.loadAnimation(requireContext(), R.anim.float_symbol4)
        symbolX.startAnimation(floatAnim)
        plus.startAnimation(floatAnim2)
        minus.startAnimation(floatAnim23)
        div.startAnimation(floatAnim234)
        return view
    }


}