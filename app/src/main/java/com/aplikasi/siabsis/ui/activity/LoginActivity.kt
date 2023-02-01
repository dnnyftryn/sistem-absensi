package com.aplikasi.siabsis.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aplikasi.siabsis.databinding.ActivityLoginBinding
import com.aplikasi.siabsis.ui.activity.hrd.HRDActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()



        binding.btnSLogin.setOnClickListener {
            val email = binding.etSEmailAddress.text.toString().trim()
            val password = binding.etSPassword.text.toString().trim()
            when{
                email.isEmpty() -> binding.etSEmailAddress.error = "Email tidak boleh kosong"
                password.isEmpty() -> binding.etSPassword.error = "Password tidak boleh kosong"
                else -> {
                    loginUser(email, password)
                }
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Selamat datang ${task.result.user}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Selamat datang HRD", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HRDActivity::class.java))
                    finish()
                }
            }
    }
}