package com.afrinacapstone.ecocraft

import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class IntroAnimationActivity : AppCompatActivity() {

    private lateinit var logoImage: ImageView
    private lateinit var turnTrashText: TextView
    private lateinit var intoTreasureText: TextView
    private lateinit var introImage: ImageView
    private lateinit var nextButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro_animation)

        logoImage = findViewById(R.id.logo_image)
        turnTrashText = findViewById(R.id.turn_trash_text)
        intoTreasureText = findViewById(R.id.into_treasure_text)
        introImage = findViewById(R.id.intro_image)
        nextButton = findViewById(R.id.next_button)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        startIntroAnimation()
    }

    private fun startIntroAnimation() {
        val introImageAnim = ObjectAnimator.ofFloat(introImage, "translationX", -500f, 500f)
        introImageAnim.duration = 2000
        introImageAnim.start()
    }
}
