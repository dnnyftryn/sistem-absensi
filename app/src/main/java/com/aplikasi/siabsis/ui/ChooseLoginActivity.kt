package com.aplikasi.siabsis.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aplikasi.siabsis.databinding.ActivityChooseLoginBinding
import com.aplikasi.siabsis.ui.murid.LoginMuridActivity
import com.aplikasi.siabsis.ui.guru.LoginGuruActivity

class ChooseLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChooseLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.cardStudent.setOnClickListener {
            startActivity(Intent(this, LoginGuruActivity::class.java))
        }

        binding.cardTeacher.setOnClickListener {
            startActivity(Intent(this, LoginMuridActivity::class.java))
        }
    }
}