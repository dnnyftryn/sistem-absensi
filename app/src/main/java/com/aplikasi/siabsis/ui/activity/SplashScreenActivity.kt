package com.aplikasi.siabsis.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowInsetsController
import com.aplikasi.siabsis.R
import com.aplikasi.siabsis.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3000)

        supportActionBar?.hide()

        //fullscreen
        controlWindowInsets(true)
    }

    private fun controlWindowInsets(isFullscreen: Boolean) {
        val insetsController = window.decorView.windowInsetsController ?: return
        insetsController.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        if (isFullscreen) {
            insetsController.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            insetsController.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        }
    }
}