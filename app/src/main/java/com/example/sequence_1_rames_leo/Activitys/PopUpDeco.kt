package com.example.sequence_1_rames_leo.Activitys

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import com.example.sequence_1_rames_leo.R

class PopUpDeco : AppCompatActivity() {
    private lateinit var TextPopUp: TextView
    private lateinit var btnOK: TextView
    private var darkStatusBar = false
    private lateinit var popup_window_Background: ConstraintLayout
    private lateinit var popup_window_view_with_border : CardView


    override fun onCreate(savedInstanceState: Bundle?) {

        //Création
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_pop_up_deco)

        //Get and Set values
        val bundle = intent.extras
        TextPopUp = findViewById(R.id.popup_window_text)
        TextPopUp.text = bundle?.getString("popuptext", "Text")
        btnOK = findViewById(R.id.popup_BtnOK)
        darkStatusBar = bundle?.getBoolean("darkstatusbar", false) ?: false
        popup_window_Background = findViewById(R.id.popup_window_background)
        popup_window_view_with_border = findViewById(R.id.popup_window_view_with_border)


        // Status bar
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // If you want dark status bar, set darkStatusBar to true
                if (darkStatusBar) {
                    this.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
                this.window.statusBarColor = Color.TRANSPARENT
                setWindowFlag(this, false)
            }
        }

        //fade animation
        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            popup_window_Background.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()

        popup_window_view_with_border.alpha = 0f
        popup_window_view_with_border.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()


    }

    private fun alerter(s: String) {

        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        t.show()

    }

    private fun setWindowFlag(activity: Activity, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags =
                winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        win.attributes = winParams
    }


    fun OnClickPopUp(v: View) {
        when (v!!.id) {



            R.id.popup_BtnOK -> {
                alerter("click sur non")
                val alpha = 100 // between 0-255
                val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
                val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
                colorAnimation.duration = 500 // milliseconds
                colorAnimation.addUpdateListener { animator ->
                    popup_window_Background.setBackgroundColor(
                        animator.animatedValue as Int
                    )
                }

                // Fade animation for the Popup Window when you press the back button
                popup_window_view_with_border.animate().alpha(0f).setDuration(500).setInterpolator(
                    DecelerateInterpolator()
                ).start()

                // After animation finish, close the Activity
                colorAnimation.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        finish()
                        overridePendingTransition(0, 0)
                    }
                })
                colorAnimation.start()

                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }

    }
}