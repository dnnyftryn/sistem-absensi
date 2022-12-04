package com.aplikasi.siabsis.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aplikasi.siabsis.databinding.ActivityChooseLoginBinding

class ChooseLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.cardStudent.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.cardTeacher.setOnClickListener {
            startActivity(Intent(this, LoginActivity2::class.java))
        }
    }
}