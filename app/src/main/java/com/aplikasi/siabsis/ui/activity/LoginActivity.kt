package com.aplikasi.siabsis.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aplikasi.siabsis.databinding.ActivityLoginBinding
import com.aplikasi.siabsis.pref.UserPreference
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var pref: UserPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        pref = UserPreference(this)

        supportActionBar?.hide()

        binding.btn1.setOnClickListener {
            startActivity(Intent(this, ScannerActivity::class.java))
        }

        binding.btnSLogin.setOnClickListener {
            val email = binding.etLEmailAddress.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()) {
                binding.etLEmailAddress.error = "Email tidak boleh kosong"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                binding.etPassword.error = "Password tidak boleh kosong dan harus lebih dari 6 karakter"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }
            when  {
                loginUser(email, password) -> {
                    Toast.makeText(this, "Anda Berhasil Login Siswa", Toast.LENGTH_SHORT).show()
                }
//                loginGuru(email, password)
            }

        }
    }


    private fun loginUser(email: String, password: String): Boolean {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
                    pref.setUser(user)
                    pref.setLogin(true)
//                    Log.d("LoginActivity", "Login User: ${user?.email}")
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "Failed to Login", Toast.LENGTH_SHORT).show()
                }
            }
        return true
    }
}