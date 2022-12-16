package com.aplikasi.siabsis.ui.guru

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasi.siabsis.API.RetrofitClient
import com.aplikasi.siabsis.data.ResultUserResponse
import com.aplikasi.siabsis.databinding.ActivityLogin2Binding
import com.aplikasi.siabsis.ui.adapter.PhotosAdapter
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Response

class LoginGuruActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogin2Binding


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener {
            if (binding.etEmail.text.toString() == "admin" && binding.etPassword.text.toString() == "admin"){
                startActivity(Intent(this, DashboardGuruActivity::class.java))
            } else {
                Snackbar.make(binding.root, "Nama pengguna atau kata sandi salah!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}