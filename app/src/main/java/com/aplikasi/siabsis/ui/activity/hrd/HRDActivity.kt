package com.aplikasi.siabsis.ui.activity.hrd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aplikasi.siabsis.databinding.ActivityHrdactivityBinding
import com.aplikasi.siabsis.pref.UserPreference
import com.aplikasi.siabsis.ui.activity.LoginActivity
import java.util.prefs.Preferences

class HRDActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHrdactivityBinding
    private lateinit var preference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHrdactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        preference = UserPreference(this)

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
            preference.removeData()
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}