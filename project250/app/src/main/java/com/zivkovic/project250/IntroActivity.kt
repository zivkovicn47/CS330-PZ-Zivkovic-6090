package com.zivkovic.project250

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.zivkovic.project250.ui.feature.intro.IntroScreen

class IntroActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            IntroScreen {
                startActivity(
                    Intent(this@IntroActivity, MainActivity::class.java)
                )
                finish()
            }
        }
    }
}
