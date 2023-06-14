package com.aplikasi.siabsis.ui.activity.hrd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.aplikasi.siabsis.databinding.ActivityDetailAbsenBinding

class DetailAbsenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailAbsenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAbsenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        val intent = intent
        val absen = intent.getStringExtra("key")
        Log.d("TAG", "onCreate: $absen")
    }
}