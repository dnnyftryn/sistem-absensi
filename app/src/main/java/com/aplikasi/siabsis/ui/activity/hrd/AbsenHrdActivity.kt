package com.aplikasi.siabsis.ui.activity.hrd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aplikasi.siabsis.databinding.ActivityAbsenHrdBinding
import com.aplikasi.siabsis.databinding.ActivityHrdactivityBinding

class AbsenHrdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAbsenHrdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAbsenHrdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
    }
}