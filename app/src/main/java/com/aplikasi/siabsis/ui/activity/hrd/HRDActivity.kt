package com.aplikasi.siabsis.ui.activity.hrd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aplikasi.siabsis.databinding.ActivityHrdactivityBinding
import com.aplikasi.siabsis.ui.activity.LoginActivity

class HRDActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHrdactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHrdactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        binding.cardViewKaryawan.setOnClickListener {
            startActivity(Intent(this, KaryawanActivity::class.java))
        }

        binding.cardViewAbsensi.setOnClickListener {
            startActivity(Intent(this, AbsenHrdActivity::class.java))
        }

        binding.cardViewRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.cardViewMaps.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        binding.cardViewLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}