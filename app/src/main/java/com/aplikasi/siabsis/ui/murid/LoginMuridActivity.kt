package com.aplikasi.siabsis.ui.murid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.aplikasi.siabsis.databinding.ActivityLoginBinding
import com.aplikasi.siabsis.pref.UserPreference
import com.aplikasi.siabsis.ui.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginMuridActivity : AppCompatActivity() {
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

        binding.btnSLogin.setOnClickListener {
            val email = binding.etLEmailAddress.text.toString()
            val password = binding.etPassword.text.toString()

            when {
                email.isEmpty() -> {
                    binding.etLEmailAddress.error = "Email tidak boleh kosong"
                    binding.etPassword.requestFocus()
                    return@setOnClickListener
                }

                password.isEmpty() || password.length < 6 -> {
                    binding.etPassword.error = "Password tidak boleh kosong dan harus lebih dari 6 karakter"
                    binding.etPassword.requestFocus()
                    return@setOnClickListener
                }
                else -> lifecycleScope.launch {
//                    loginUser(email, password)
                    if (binding.etLEmailAddress.text.toString() == "admin" && binding.etPassword.text.toString() == "admin"){
                        startActivity(Intent(this@LoginMuridActivity, MainActivity::class.java))
                    } else {
                        Toast.makeText(this@LoginMuridActivity, "Nama pengguna atau kata sandi salah!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }


    private fun loginUser(email: String, password: String): Boolean {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
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