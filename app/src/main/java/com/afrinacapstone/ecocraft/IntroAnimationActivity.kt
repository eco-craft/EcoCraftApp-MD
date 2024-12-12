package com.afrinacapstone.ecocraft

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.afrinacapstone.ecocraft.ui.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroAnimationActivity : AppCompatActivity() {

    private lateinit var logoImage: ImageView
    private lateinit var turnTrashText: TextView
    private lateinit var intoTreasureText: TextView
    private lateinit var introImage: ImageView
    private lateinit var nextButton: ImageView

    private var isUserLoggedIn = false
    private val viewModel by viewModels<IntroAnimationViewModel>()

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

        lifecycleScope.launch {
            viewModel.isUserLoggedIn().collect {
                isUserLoggedIn = it
            }
        }

        nextButton.setOnClickListener {
            val loginIntent = if (isUserLoggedIn) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)

            }
            startActivity(loginIntent)
            finish()
        }
    }

    private fun startIntroAnimation() {
        val introImageAnim = ObjectAnimator.ofFloat(introImage, "translationX", -500f, 60f)
        introImageAnim.duration = 2000
        introImageAnim.start()
    }
}
