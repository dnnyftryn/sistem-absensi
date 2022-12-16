package com.aplikasi.siabsis.ui.guru

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aplikasi.siabsis.databinding.ActivityDashboardGuruBinding

class DashboardGuruActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardGuruBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardGuruBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}